package com.example.flickrphotosearch.model

import kotlinx.serialization.Serializable

@Serializable
data class PhotoData(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val isPublic: Int? = null,
    val isFriend: Int? = null,
    val isFamily: Int? = null
)

@Serializable
data class PhotoList(
    val photos: Photos
)

@Serializable
data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<PhotoData>
)