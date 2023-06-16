package com.example.appmarvel.data.marvelresponse

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)