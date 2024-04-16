package com.example.music_application

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import it.unipd.dei.music_application.R

//import com.example.music_application.databinding.ContentMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}