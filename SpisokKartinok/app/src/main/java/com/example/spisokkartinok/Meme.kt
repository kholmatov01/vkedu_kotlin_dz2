package com.example.spisokkartinok

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meme(
    @SerialName("id") val id: Int,
    @SerialName("description") val description: String,
    @SerialName("url") val url: String,
    @SerialName("type") val type: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("ratio") val ratio: Double,
)