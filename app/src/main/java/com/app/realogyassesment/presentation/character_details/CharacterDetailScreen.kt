package com.app.realogyassesment.presentation.character_details

import androidx.compose.runtime.Composable
import com.app.realogyassesment.domain.model.CharacterObject
import com.app.realogyassesment.presentation.character_details.component.CharacterDetail

@Composable
fun CharacterDetailScreen(
    characterObject: CharacterObject?
) {
    if(characterObject != null) {
        CharacterDetail(characterObject = characterObject)
    }
}