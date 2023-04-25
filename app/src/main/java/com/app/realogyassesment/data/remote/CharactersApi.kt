package com.app.realogyassesment.data.remote

import com.app.realogyassesment.BuildConfig
import com.app.realogyassesment.data.remote.dto.CharacterResponse
import retrofit2.http.GET

interface CharactersApi {

    @GET(BuildConfig.QUERY)
    suspend fun getCharacters(): CharacterResponse
}