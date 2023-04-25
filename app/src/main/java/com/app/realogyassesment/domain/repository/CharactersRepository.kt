package com.app.realogyassesment.domain.repository

import com.app.realogyassesment.data.remote.dto.RelatedTopic

interface CharactersRepository {

    //TODO: look into renaming related topic
    suspend fun getCharacters(): List<RelatedTopic>
}