package com.example.myapplication


import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookManager(private val context: Context, private val lifecycleScope: LifecycleCoroutineScope) {
    private lateinit var books: ArrayList<Book>
    private lateinit var bookDao: BookDao

    fun init() {
        val db = AppDatabase.getDatabase(context)
        bookDao = db.bookDao()

        lifecycleScope.launch(Dispatchers.IO) {
            books = ArrayList(bookDao.getAllBooks())
        }
    }

    fun addBook(book: Book) {
        lifecycleScope.launch(Dispatchers.IO) {
            bookDao.insertBook(book)
            withContext(context = Dispatchers.Main) {
                updateBookList()
            }
        }
    }

    fun deleteSelectedBooks(selectedIds: BooleanArray) {
        val booksToRemove = mutableListOf<Book>()
        for (i in selectedIds.indices) {
            if (selectedIds[i]) {
                booksToRemove.add(books[i])
            }
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                bookDao.deleteBooks(booksToRemove)
            }
            books.removeAll(booksToRemove.toSet())
            books.ensureCapacity(books.size + booksToRemove.size)
        }
    }

    fun getAllBooks(): ArrayList<Book> {
        return books
    }

    private fun updateBookList() {
        lifecycleScope.launch(Dispatchers.IO) {
            books = ArrayList(bookDao.getAllBooks())
        }
    }
}
