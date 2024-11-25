package com.example.spisokkartinok

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.spisokkartinok.ui.theme.SpisokKartinokTheme
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val retrofitController by lazy { com.example.spisokkartinok.RetrofitController(BASE_URL) }

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpisokKartinokTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlowRow(
                        modifier = Modifier

                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.Center,


                        ) {

                    }

                }
            }
        }
    }



}

@Composable
fun KartinkaScreen(retrofitController: RetrofitController) {
    var kartinki_list by rememberSaveable { mutableStateOf<List<Meme>?>(null) }
    var is_loading by rememberSaveable { mutableStateOf(false) }
    var has_error by rememberSaveable { mutableStateOf(false) }
    val is_landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val coroutine_scope = rememberCoroutineScope()
    val handler = CoroutineExceptionHandler { _, exception ->
        run {
            is_loading = false
            has_error = true
            Toast.makeText(baseContext, exception.message ?: "", Toast.LENGTH_LONG).show()
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        when {
            is_loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            has_error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Box(modifier = Modifier.clickable(onClick = {
                            showKartinka(
                                coroutine_scope,
                                retrofitController,
                                handler,
                                { kartinkaList = it },
                                { is_loading = it },
                                { has_error = it },
                                baseContext
                            )

                        })) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = getString(R.string.refresh),
                                tint = Color.Red, modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }

            else -> {
                if (isLandscape) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(10.dp)
                    )
                    {
                        items(gifList ?: emptyList()) { gifData ->
                            OneGifItem(gifData)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    )
                    {
                        items(gifList ?: emptyList()) { gifData ->
                            OneGifItem(gifData)
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                coroutineScope.launch(handler) {}
                showTrendingGifs(
                    coroutineScope,
                    giphyRepository,
                    BuildConfig.API_KEY,
                    handler,
                    { gifList = it },
                    { isLoading = it },
                    { hasError = it },
                    baseContext
                )
            }, modifier = Modifier.weight(1f)) {
                Text(text = getString(R.string.updateGifsTrending))
            }
        }
    }
}

fun showKartinka (
    coroutine_scope: CoroutineScope,
    retrofit_controller: RetrofitController,
    handler: CoroutineExceptionHandler,
    kartinki_list: (List<Meme>?) -> Unit,
    set_loading: (Boolean) -> Unit,
    set_error: (Boolean) -> Unit,
    base_context: Context
) {
    coroutine_scope.launch(handler) {
        set_loading(true)
        set_error(false)
        try {
            val response = retrofit_controller.getKartinki(10)
            kartinki_list(response?.data)
        } catch (e: Exception) {
            Toast.makeText(base_context, e.localizedMessage, Toast.LENGTH_SHORT).show()
            set_error(true)
        } finally {
            set_loading(false)
        }
    }
}

@Composable
fun KartinkaCard(uri: String) {

}

const val API_KEY = "df246e1a3cmsh33c6b29515b11b4p113f8djsnaba95bb6f6d8"

//если кто то тырит у меня работу пж возьмите свой ключ, там использований конечное кол-во в день
const val API_HOST = "humor-jokes-and-memes.p.rapidapi.com"
