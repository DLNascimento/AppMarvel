package com.example.appmarvel.data.api

import com.example.appmarvel.data.marvelresponse.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {
    @GET("characters")
    suspend fun getCharacters(
        @Query("nameStartsWith")nameStartsWith: String? = null
    ): Response<CharacterResponse>


}

