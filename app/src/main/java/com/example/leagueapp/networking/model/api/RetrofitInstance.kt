package com.example.leagueapp.networking.model.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private var apiBaseURL = "https://eun1.api.riotgames.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RiotApi by lazy {
        retrofit.create(RiotApi::class.java)
    }


    private var builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(apiBaseURL)
            .build()

    val apige = builder.create(RiotApi::class.java)

    fun changeApiBaseUrl(newApiBaseUrl : String){
        apiBaseURL = newApiBaseUrl

        builder = null
        builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiBaseURL)
                .build()

        Log.d("Wow", "New url ${builder.baseUrl().toString()}")
    }
}