package com.example.testlibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.myapplication.R

class BookListAdapter(
    context: Context,
    private val books: ArrayList<Book>
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    val selectedIds = BooleanArray(books.size)

    override fun getCount(): Int {
        return books.size
    }

    override fun getItem(position: Int): Any {
        return books[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.book_list_item, parent, false)
        }

        val book = books[position]
        val titleTextView = view!!.findViewById<TextView>(R.id.titleTextView)
        val authorTextView = view.findViewById<TextView>(R.id.authorTextView)

        titleTextView.text = book.title
        authorTextView.text = book.author

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        checkBox.isChecked = selectedIds[position]
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            selectedIds[position] = isChecked
        }

        return view
    }
}
