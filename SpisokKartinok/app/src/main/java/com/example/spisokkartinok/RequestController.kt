package com.example.spisokkartinok

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface RequestController {
    suspend fun requestKartinka(): Result
}

sealed interface Result {
    data class Ok(val joke: Kartinka) : Result
    data class Error(val error: String) : Result
}

@Serializable
data class Kartinka(
    @SerialName("value") val kartinkaUrl: String,
)
