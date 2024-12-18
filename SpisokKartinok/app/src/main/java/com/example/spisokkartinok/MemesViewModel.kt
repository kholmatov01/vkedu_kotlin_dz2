package com.example.spisokkartinok

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MemesViewModel: ViewModel() {

    val memesList = mutableStateListOf<Meme>()
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun fetchMemes(number: Int = 1) {
        if (isLoading) return
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = ""
                val newMeme = RetrofitController.getMeme(number)
                if (newMeme != null) {
                    memesList.add(newMeme)
                }
            } catch (e: Exception) {
                errorMessage = e.message!!
            } finally {
                isLoading = false
            }
        }
    }
}