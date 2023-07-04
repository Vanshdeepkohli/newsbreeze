package com.example.newsbreeze.api

import com.example.newsbreeze.data.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country") country: String = "in",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = "79e51858864d430c85c8221e9f57afdd"
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") topic: String,
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = "79e51858864d430c85c8221e9f57afdd"
    ): Response<NewsResponse>
}