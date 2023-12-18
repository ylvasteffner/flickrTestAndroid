package com.example.flickrphotosearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException

class SearchPhotosViewModel : ViewModel() {

    var state by mutableStateOf<SearchPhotosState>(
        SearchPhotosState.Data(
            PhotoSearchData(
                null,
                null
            )
        )
    )
        private set

    var emptyKeyWordError by mutableStateOf<String?>(null)

    fun searchForImages(keyWord: String) {
        if (keyWord.isNotEmpty() && keyWord.isNotBlank()) {
            state = SearchPhotosState.Loading
            emptyKeyWordError = null
            viewModelScope.launch {
                try {
                    val searchResponse = searchForImagesKtor(keyWord)
                    val photosList = searchResponse.photos.photo.map { photo ->
                        Photo(
                            url = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                            title = photo.title
                        )
                    }
                    val data = PhotoSearchData(keyWord, photosList)
                    state = SearchPhotosState.Data(data)

                } catch (e: Exception) {
                    state = SearchPhotosState.Error
                    handleSearchException(e)
                }
            }
        } else {
            emptyKeyWordError = "Please enter text to search"
        }
    }

    fun handleSearchException(e: Throwable) {
        when (e) {
            is ClientRequestException -> Timber.e("ClientRequestException: ${e.message}")
            is ServerResponseException -> Timber.e("ServerResponseException: ${e.message}")
            is ConnectException -> Timber.e("ConnectException: ${e.message}")
            is JsonConvertException -> Timber.e("JsonConvertException: ${e.message}")
            is SocketTimeoutException -> Timber.e("SocketTimeoutException: ${e.message}")
            else -> Timber.e("Unexpected exception occured: ${e.message}")
        }
    }

    open class SearchPhotosState {
        object Loading : SearchPhotosState()
        object Error : SearchPhotosState()
        data class Data(val data: PhotoSearchData) : SearchPhotosState()
    }
}

data class Photo(
    val url: String,
    val title: String
)

data class PhotoSearchData(
    val searchTerm: String?,
    val listOfPhotos: List<Photo>?
)

