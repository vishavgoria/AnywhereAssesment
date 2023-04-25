package com.app.realogyassesment.data.repository

import com.app.realogyassesment.data.remote.CharactersApi
import com.app.realogyassesment.data.remote.dto.RelatedTopic
import com.app.realogyassesment.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val api: CharactersApi
) : CharactersRepository {

    override suspend fun getCharacters(): List<RelatedTopic> {
        return api.getCharacters().RelatedTopics
    }
}