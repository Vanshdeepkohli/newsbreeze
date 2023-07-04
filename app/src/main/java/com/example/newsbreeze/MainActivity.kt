package com.example.newsbreeze

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.newsbreeze.database.NewsArticleDatabase
import com.example.newsbreeze.repository.NewsRepository
import com.example.newsbreeze.viewModel.NewsViewModel
import com.example.newsbreeze.viewModel.NewsViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val respository = NewsRepository(NewsArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, respository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

    }
}