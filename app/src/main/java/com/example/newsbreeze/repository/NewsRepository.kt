package com.example.newsbreeze.repository

import com.example.newsbreeze.api.RetrofitInstance
import com.example.newsbreeze.data.Article
import com.example.newsbreeze.database.NewsArticleDatabase
import retrofit2.Retrofit

class NewsRepository(private val db: NewsArticleDatabase) {

    suspend fun getNews(country: String, page: Int) = RetrofitInstance.api.getNews(country, page)


    suspend fun searchNews(topic: String, page: Int) = RetrofitInstance.api.searchNews(topic, page)


    suspend fun insert(article: Article) = db.getNewsDao().insert(article)


    suspend fun deleteArticle(article: Article) = db.getNewsDao().deleteArticle(article)


    fun getAllSavedArticles() = db.getNewsDao().getAllSavedArticles()

    fun deleteAll() = db.getNewsDao().deleteAll()

}