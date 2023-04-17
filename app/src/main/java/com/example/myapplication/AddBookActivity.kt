package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddBookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val author = findViewById<EditText>(R.id.authorEditText).text.toString()
            val description = findViewById<EditText>(R.id.descriptionEditText).text.toString()

            val intent = Intent()
            intent.putExtra("title", title)
            intent.putExtra("author", author)
            intent.putExtra("description", description)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}