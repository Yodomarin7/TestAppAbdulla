package com.example.testappabdulla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    lateinit var toolBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolBar)
    }
}























