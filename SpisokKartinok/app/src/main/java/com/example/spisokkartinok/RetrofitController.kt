package com.example.spisokkartinok

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitController {
    private const val BASE_URL = "https://humor-jokes-and-memes.p.rapidapi.com"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(MemesApi::class.java)

    suspend fun getMemes(number: Int): List<Meme>? {
        val response = api.getMemes(number)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            }
        }
        return null
    }

}
