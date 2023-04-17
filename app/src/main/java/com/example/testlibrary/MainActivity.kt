package com.example.testlibrary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var books: ArrayList<Book>
    private lateinit var listView: ListView
    private lateinit var adapter: BookListAdapter
    private lateinit var bookDao: BookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupListeners()

        val db = AppDatabase.getDatabase(this)
        bookDao = db.bookDao()

        lifecycleScope.launch(Dispatchers.IO) {
            books = ArrayList(bookDao.getAllBooks())

            withContext(Dispatchers.Main) {
                adapter = BookListAdapter(this@MainActivity, books)
                listView.adapter = adapter
            }
        }
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
            books.forEachIndexed { index, book ->
                if (adapter.selectedIds[index]) {
                    val intent = Intent(this, ViewBookActivity::class.java)
                    intent.putExtra("title", book.title)
                    intent.putExtra("author", book.author)
                    intent.putExtra("description", book.description)
                    startActivity(intent)
                    return@setOnClickListener
                }
            }
        }

        val deleteButton: Button = findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            val booksToRemove = mutableListOf<Book>()
            for (i in adapter.selectedIds.indices) {
                if (adapter.selectedIds[i]) {
                    booksToRemove.add(books[i])
                }
            }

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    bookDao.deleteBooks(booksToRemove)
                }
                books.removeAll(booksToRemove.toSet())
                adapter.notifyDataSetChanged()

            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (books.isEmpty()) {
                // список пуст, создаем новый список
                books = ArrayList()
            }
            val title = data?.getStringExtra("title") ?: ""
            val author = data?.getStringExtra("author") ?: ""
            val description = data?.getStringExtra("description") ?: ""
            val newBook = Book(title = title, author = author, description = description)
            lifecycleScope.launch(Dispatchers.IO) {
                bookDao.insertBook(newBook)
                withContext(Dispatchers.Main) {
                    books.add(newBook)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}
