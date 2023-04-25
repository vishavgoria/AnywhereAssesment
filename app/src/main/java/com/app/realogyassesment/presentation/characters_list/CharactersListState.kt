package com.app.realogyassesment.presentation.characters_list

import com.app.realogyassesment.domain.model.CharacterObject

data class CharactersListState(
    val isLoading: Boolean = false,
    val characterObjects: List<CharacterObject> = emptyList(),
    val error: String = ""
)