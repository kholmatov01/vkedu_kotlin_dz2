package com.example.spisokkartinok

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

data class Gif(var url: String)


/*
fun main() {
    val request = Request.Builder()
        .url("https://giphy.p.rapidapi.com/v1/gifs/random?api_key=dc6zaTOxFJmzC")
        .get()
        .addHeader("x-rapidapi-key", "df246e1a3cmsh33c6b29515b11b4p113f8djsnaba95bb6f6d8")
        .addHeader("x-rapidapi-host", "giphy.p.rapidapi.com")
        .build()
    val client = OkHttpClient()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) println("error = $response")

        for ((name, value) in response.headers) {
            println("$name: $value")
        }

        println("RESPONSE BODY: " + response.body!!.string())
    }
}*/
