package com.example.spisokkartinok

import android.media.Image
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.http.GET

interface KartinkaApi {
    @GET("humor-jokes-and-memes.p.rapidapi.com/memes/random?number=1&media-type=image")
    suspend fun kartinka(): Response<Kartinka>
}
fun main() {
    val request = Request.Builder()
        .url("https://humor-jokes-and-memes.p.rapidapi.com/memes/random?number=1&media-type=image")
        .get()
        .addHeader("x-rapidapi-key", "df246e1a3cmsh33c6b29515b11b4p113f8djsnaba95bb6f6d8")
        .addHeader("x-rapidapi-host", "humor-jokes-and-memes.p.rapidapi.com")
        .build()
    val client = OkHttpClient()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) println("error = $response")

        for ((name, value) in response.headers) {
            println("$name: $value")
        }

        println("RESPONSE BODY: " + response.body!!.string())
    }
}
