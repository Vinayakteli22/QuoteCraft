package com.example.inspireverse

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL="https://zenquotes.io/api/"


    private fun getInstance():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //create a instance of Quote api
    val quoteApi:QuoteApi= getInstance().create(QuoteApi::class.java)
}