package com.example.spisokkartinok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter

class MemesFragment : Fragment() {
    private val viewModel: MemesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MemesScreen(viewModel)
            }
        }
    }
}

@Composable
fun MemesScreen(viewModel: MemesViewModel = viewModel()) {
    val lazyState = rememberLazyStaggeredGridState()

    LaunchedEffect(Unit) {
        if (viewModel.memesList.isEmpty()) {
            viewModel.fetchMemes()
        }
    }

    LaunchedEffect(lazyState) {
        snapshotFlow { lazyState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (!viewModel.isLoading && lastVisibleIndex == viewModel.memesList.size - 1) {
                    viewModel.fetchMemes()
                }
            }
    }

    if (viewModel.isLoading && viewModel.memesList.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else if (viewModel.errorMessage.isNotEmpty() && viewModel.memesList.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = viewModel.errorMessage, modifier = Modifier.padding(16.dp))
                Button(onClick = { viewModel.fetchMemes() }) {
                    Text("Повторить")
                }
            }
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            state = lazyState,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            itemsIndexed(viewModel.memesList) { index, meme ->
                val imageHeight = meme.width.toFloat() / meme.height.toFloat()
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .aspectRatio(imageHeight)
                        .clip(RoundedCornerShape(15.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(meme.url),
                        contentDescription = "Photo from Unsplash",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        if (viewModel.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}