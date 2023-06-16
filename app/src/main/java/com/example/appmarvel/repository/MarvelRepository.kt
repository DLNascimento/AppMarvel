package com.example.appmarvel.repository

import com.example.appmarvel.data.api.ServiceApi
import javax.inject.Inject

class MarvelRepository @Inject constructor(private val api: ServiceApi) {

    suspend fun getCharacters(nameStartsWith: String? = null) = api.getCharacters(nameStartsWith)

}