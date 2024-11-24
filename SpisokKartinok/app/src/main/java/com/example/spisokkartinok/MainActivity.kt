package com.example.spisokkartinok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spisokkartinok.ui.theme.SpisokKartinokTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import retrofit.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET


data class CatResponse(val url: String)

interface CatApi {
    @GET("cat")
    suspend fun getCatImage(): CatResponse
}
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpisokKartinokTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://cataas.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val catApi = retrofit.create(CatApi::class.java)

                    var catImageUrl by remember { mutableStateOf("") }
                    var isLoading by remember { mutableStateOf(true) }

                    fun loadCatImage() {
                        CoroutineScope(Dispatchers.IO).launch {
                            isLoading = true
                            try {
                                val response = catApi.getCatImage()
                                catImageUrl = response.url
                            } catch (e: Exception) {
                                catImageUrl = ""
                            } finally {
                                isLoading = false
                            }
                        }
                    }

                    LaunchedEffect(Unit) {
                        loadCatImage()
                    }
                        FlowRow(
                            modifier = Modifier

                                .verticalScroll(rememberScrollState())
                                .padding(innerPadding)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalArrangement = Arrangement.Center,


                            ) {
                            if (isLoading) {
                                Text("Загрузка кошки...")
                            } else {
                                if (catImageUrl.isNotEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = catImageUrl),
                                        contentDescription = "Кошка",
                                        modifier = Modifier.size(200.dp)
                                    )
                                } else {
                                    Text("Не удалось загрузить кошку.")
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { loadCatImage() }) {
                                Text("Показать другую кошку")
                            }
                            /*var gifUrl = "null"

                        val client = OkHttpClient()

                        val request = Request.Builder()
                            .url("https://giphy.p.rapidapi.com/v1/gifs/random")
                            .get()
                            .addHeader("x-rapidapi-key", "dc6zaTOxFJmzC")
                            .addHeader("x-rapidapi-host", "giphy.p.rapidapi.com")
                            .build()

                        val response = client.newCall(request).execute()
                        gifUrl = response.body!!.string()
                        *//*val response = client.newCall(request).enqueue(object: Callback {
                            override fun onFailure(call: Call, e: java.io.IOException) {
                                runOnUiThread {
                                    gifUrl = "jopa"
                                }
                                e.printStackTrace()
                            }

                            override fun onResponse(call: Call, response: Response) {
                                if (response.isSuccessful) {
                                    runOnUiThread{
                                        gifUrl = response.body!!.string()
                                    }
                                }
                                else {
                                    runOnUiThread {
                                        gifUrl = "gavno"
                                    }
                                }
                            }

                        })*//*

                        Text(text = gifUrl, fontSize = 50.sp)*/
                        }
                    }
                }
            }
        }
    }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun GifCard(uri: String) {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpisokKartinokTheme {
        Greeting("Android")
    }
}