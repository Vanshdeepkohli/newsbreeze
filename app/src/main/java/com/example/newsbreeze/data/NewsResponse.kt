package com.example.newsbreeze.data

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)