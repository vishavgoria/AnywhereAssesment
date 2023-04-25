package com.app.realogyassesment.presentation

sealed class Screens(val route: String) {
    object CharactersListScreen: Screens("characters_list_screen")
    object CharacterDetailScreen: Screens("character_detail_screen/character={character}")
}