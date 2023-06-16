package com.example.appmarvel.data.marvelresponse

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)