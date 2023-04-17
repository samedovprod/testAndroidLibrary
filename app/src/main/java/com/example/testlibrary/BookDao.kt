package com.example.testlibrary

import androidx.room.*

@Dao
interface BookDao {

    @Query("SELECT * FROM books")
    fun getAllBooks(): List<Book>

    @Insert
    fun insertBook(book: Book)
    @Delete
    fun deleteBooks(books: MutableList<Book>)
}
