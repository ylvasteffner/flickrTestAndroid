package com.example.flickrphotosearch.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.flickrphotosearch.Photo
import com.example.flickrphotosearch.PhotoSearchData
import com.example.flickrphotosearch.SearchPhotosViewModel

@Composable
fun SearchPhotosPage(
    viewModel: SearchPhotosViewModel
) {
    val state = viewModel.state

    when (state) {
        is SearchPhotosViewModel.SearchPhotosState.Loading -> LoadingPageSpinner()
        is SearchPhotosViewModel.SearchPhotosState.Data -> SearchPhotos(
            data = state.data,
            searchForImages = { keyWord ->
                viewModel.searchForImages(keyWord)
            },
            emptyKeyWord = viewModel.emptyKeyWordError
        )

        is SearchPhotosViewModel.SearchPhotosState.Error -> Error(
            searchForImages = { keyWord ->
                viewModel.searchForImages(keyWord)
            }
        )
    }
}

@Composable
fun SearchPhotos(
    data: PhotoSearchData,
    searchForImages: (keyWord: String) -> Unit = { _ -> },
    emptyKeyWord: String?,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 12.dp)
    ) {
        InputSearchField(searchForImages)

        if (data.searchTerm?.isNotEmpty() == true) {
            val searchInfo = "You searched for: " + data.searchTerm
            Text(text = searchInfo)
        }

        emptyKeyWord?.let {
            Text(text = it)
        }

        Spacer(modifier = Modifier.size(12.dp))

        data.listOfPhotos?.let {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                content = {
                    items(data.listOfPhotos) { photoItem ->
                        PhotoItemRow(photoItem)
                    }
                }
            )
        } ?: run {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Enter key word to search for photos")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSearchField(
    searchForImages: (keyWord: String) -> Unit = { _ -> }
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("Enter text") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.size(5.dp))

        Button(
            onClick = { searchForImages(text) },
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
        ) {
            Text(text = "Search")
        }
    }
}

@Composable
fun PhotoItemRow(
    photoItem: Photo
) {
    val painter = rememberAsyncImagePainter(model = photoItem.url)
    Column {
        Image(
            painter = painter,
            contentDescription = photoItem.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
        )
        Text(
            text = photoItem.title,
        )
    }
}

@Composable
fun Error(
    searchForImages: (keyWord: String) -> Unit = { _ -> }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 12.dp)
    ) {
        InputSearchField(searchForImages)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Unexpected error, please try again",
                color = Color.Red
            )
        }
    }
}

@Composable
fun LoadingPageSpinner(modifier: Modifier = Modifier, content: (@Composable () -> Unit)? = null) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        if (content != null) {
            content()
        }
    }
}