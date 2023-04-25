package com.app.realogyassesment.domain.model

data class CharacterObject(
    val imageUrl: String,
    val name: String,
    val description: String,
    var isSelected: Boolean = false
)
