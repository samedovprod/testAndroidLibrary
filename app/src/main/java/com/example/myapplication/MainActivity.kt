package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: BookListAdapter
    private lateinit var bookManager: BookManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupListeners()

        bookManager = BookManager(this, lifecycleScope)
        bookManager.init()

        val books = bookManager.getAllBooks()
        adapter = BookListAdapter(this, books)
        listView.adapter = adapter
    }

    private fun setupViews() {
        listView = findViewById(R.id.listView)
    }

    private fun setupListeners() {
        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            adapter.selectedIds.fill(false)
            val intent = Intent(this, AddBookActivity::class.java)
            startActivityForResult(intent, 1)
        }

        val viewButton: Button = findViewById(R.id.viewButton)
        viewButton.setOnClickListener {
            val selectedBook = getSelectedBook()
            if (selectedBook != null) {
                val intent = Intent(this, ViewBookActivity::class.java)
                intent.putExtra("title", selectedBook.title)
                intent.putExtra("author", selectedBook.author)
                intent.putExtra("description", selectedBook.description)
                startActivity(intent)
            }
        }

        val deleteButton: Button = findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            val selectedIds = adapter.selectedIds
            bookManager.deleteSelectedBooks(selectedIds)
            adapter.notifyDataSetChanged()
        }
    }

    private fun getSelectedBook(): Book? {
        val selectedIds = adapter.selectedIds
        for (i in selectedIds.indices) {
            if (selectedIds[i]) {
                val book = adapter.getItem(i)
                if (book is Book) {
                    return book
                }
            }
        }
        return null
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra("title") ?: ""
            val author = data?.getStringExtra("author") ?: ""
            val description = data?.getStringExtra("description") ?: ""
            val newBook = Book(title = title, author = author, description = description)
            bookManager.addBook(newBook)
            adapter.notifyDataSetChanged()
        }
    }
}

