package com.example.flickrphotosearch

import com.example.flickrphotosearch.model.PhotoList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


val client = HttpClient(CIO) {
    install(Logging)
    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = true
                prettyPrint = true
                ignoreUnknownKeys = true // Ignore unknown keys during deserialization
            }
        )
    }
    expectSuccess = true
}

suspend fun searchForImagesKtor(
    keyWord: String,
): PhotoList {

    val response: PhotoList = client.get {
        url {
            protocol = URLProtocol.HTTPS
            host = "www.flickr.com"
            encodedPath = "/services/rest/"
            parameters.append("method", "flickr.photos.search")
            parameters.append("api_key", BuildConfig.flickrApiKey)
            parameters.append("text", keyWord)
            parameters.append("format", "json")
            parameters.append("nojsoncallback", "1")
        }
    }.body()
    return response
}