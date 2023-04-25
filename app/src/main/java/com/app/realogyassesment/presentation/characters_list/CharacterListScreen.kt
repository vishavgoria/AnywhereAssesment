package com.app.realogyassesment.presentation.characters_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.realogyassesment.common.Constants.CHARACTER_KEY
import com.app.realogyassesment.domain.model.CharacterObject
import com.app.realogyassesment.presentation.Screens
import com.app.realogyassesment.presentation.character_details.component.CharacterDetail
import com.app.realogyassesment.presentation.characters_list.component.CharacterItem
import com.app.realogyassesment.presentation.characters_list.component.SearchBarComponent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.squareup.moshi.Moshi
import java.net.URLEncoder

@Composable
fun CharactersListScreen(
    navController: NavController,
    viewModel: CharactersListViewModel = hiltViewModel(),
    showOnePane: Boolean,
) {
    val state = viewModel.state.value
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val query = remember { mutableStateOf("") }

    if (showOnePane) {
        ShowOnePaneScreen(
            navController = navController,
            viewModel = viewModel,
            state = state,
            isRefreshing = isRefreshing,
            query = query
        )
    } else {
        ShowTwoPaneScreen(
            viewModel = viewModel,
            state = state,
            isRefreshing = isRefreshing,
            query = query
        )
    }
}

@Composable
fun ShowOnePaneScreen(
    navController: NavController,
    viewModel: CharactersListViewModel,
    state: CharactersListState,
    isRefreshing: Boolean,
    query: MutableState<String>
) {
    Column() {
        SearchBarComponent(query = query)

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.refreshCharacters() },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.characterObjects) { character ->
                    CharacterItem(
                        characterObject = character,
                        onItemClick = {
                            onCharacterClick(navController, character, viewModel, query)
                        }
                    )
                }
            }

            if (state.characterObjects.isEmpty() && !state.isLoading && state.error.isBlank()) {
                Text("No results", modifier = Modifier.padding(8.dp))
            }

            if (state.error.isNotBlank()) {
                ErrorComponent(state.error)
            }

            if (state.isLoading) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Center))
                }
            }
        }
    }
}

@Composable
fun ShowTwoPaneScreen(
    viewModel: CharactersListViewModel,
    state: CharactersListState,
    isRefreshing: Boolean,
    query: MutableState<String>
) {
    val selectedCharacter = viewModel.selectedCharacter
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.35f)
        ) {
            SearchBarComponent(query = query)

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { viewModel.refreshCharacters() },
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.characterObjects.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.characterObjects) { character ->
                            CharacterItem(
                                characterObject = character,
                                onItemClick = {
                                    viewModel.onCharacterSelected(character)
                                    updateSelectedCharacter(state.characterObjects, character)
                                }
                            )
                        }
                    }
                }

                if (state.characterObjects.isEmpty() && !state.isLoading && state.error.isBlank()) {
                    Text("No results", modifier = Modifier.padding(8.dp))
                }

                if (state.error.isNotBlank()) {
                    ErrorComponent(state.error)
                }

                if (state.isLoading) {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Center))
                    }
                }
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = Color.LightGray,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedCharacter.value.name.isNotEmpty() &&
                selectedCharacter.value.description.isNotEmpty()
            ) {
                CharacterDetail(characterObject = selectedCharacter.value, fontSize = 24)
            } else {
                Text(text = "No character selected", fontSize = 30.sp)
            }
        }
    }
}

private fun updateSelectedCharacter(
    characters: List<CharacterObject>,
    selectedCharacter: CharacterObject
) {
    for (ch in characters) {
        if (ch != selectedCharacter) {
            ch.isSelected = false
        }
    }

    selectedCharacter.isSelected = true
}

private fun onCharacterClick(
    navController: NavController,
    item: CharacterObject,
    viewModel: CharactersListViewModel,
    query: MutableState<String>
) {
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(CharacterObject::class.java).lenient()
    val characterJson = jsonAdapter.toJson(item)
    val characterEncoded = URLEncoder.encode(characterJson, "utf-8")

    navController.navigate(
        Screens.CharacterDetailScreen.route.replace("{$CHARACTER_KEY}", characterEncoded)
    )

    viewModel.clearSearchQuery()
    query.value = ""
}

@Composable
fun ErrorComponent(error: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.Center
        )
    }
}