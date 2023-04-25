package com.app.realogyassesment.presentation.characters_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.realogyassesment.common.Resource
import com.app.realogyassesment.data.use_case.GetCharacters
import com.app.realogyassesment.domain.model.CharacterObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharacters
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val _state = mutableStateOf(CharactersListState())
    val state: State<CharactersListState> = _state

    private val _selectedCharacter = mutableStateOf(CharacterObject("", "", ""))
    val selectedCharacter: State<CharacterObject> = _selectedCharacter

    private var charactersList: List<CharacterObject> = emptyList()

    init {
        getCharacters()
    }

    fun refreshCharacters() {
        getCharacters()
        _selectedCharacter.value = CharacterObject("", "", "")
    }

    fun onCharacterSelected(selectedCharacter: CharacterObject) {
        _selectedCharacter.value = selectedCharacter
    }

    fun onSearchQueryUpdate(query: String) {
        _state.value = CharactersListState(characterObjects = charactersList.filter {
            it.name.lowercase().contains(
                query.lowercase(Locale.getDefault())
            )
        })
    }

    fun clearSearchQuery() {
        _state.value = CharactersListState(characterObjects = charactersList)
    }

    private fun getCharacters() {
        getCharactersUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    charactersList = result.data ?: emptyList()

                    _state.value = CharactersListState(characterObjects = charactersList)
                    _isRefreshing.emit(false)
                }
                is Resource.Error -> {
                    Log.d("error 1",result.message ?: "No message")
                    _state.value = CharactersListState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                    _isRefreshing.emit(false)
                }
                is Resource.Loading -> {
                    _isRefreshing.emit(true)
                    _state.value = CharactersListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}