package com.example.flickrphotosearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.flickrphotosearch.ui.layout.SearchPhotosPage
import com.example.flickrphotosearch.ui.theme.FlickrPhotoSearchTheme
import timber.log.Timber


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val mainViewModel: SearchPhotosViewModel by viewModels()

        setContent {
            FlickrPhotoSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchPhotosPage(mainViewModel)
                }
            }
        }
    }
}