package com.app.realogyassesment.presentation.characters_list.component

import android.view.KeyEvent.KEYCODE_ENTER
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.realogyassesment.BuildConfig
import com.app.realogyassesment.presentation.characters_list.CharactersListViewModel

const val MAX_CHARS_LENGTH = 35

@Composable
fun SearchBarComponent(
    viewModel: CharactersListViewModel = hiltViewModel(),
    query: MutableState<String>
) {
    val focusManager = LocalFocusManager.current
    val isSearching = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(57.dp)
            .background(color = MaterialTheme.colors.primary)
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KEYCODE_ENTER) {
                    focusManager.clearFocus()
                    true
                }
                false
            },
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (!isSearching.value) {
            TopAppBar(
                title = { Text(BuildConfig.APP_NAME) },
                actions = {
                    IconButton(onClick = { isSearching.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = ""
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        } else {
            SearchViewTextInput(
                query = query,
                viewModel = viewModel,
                isSearching = isSearching,
                focusRequester = focusRequester,
                focusManager = focusManager
            )
        }
    }
}

@Composable
fun SearchViewTextInput(
    query: MutableState<String>,
    viewModel: CharactersListViewModel,
    isSearching: MutableState<Boolean>,
    focusRequester: FocusRequester,
    focusManager: FocusManager
) {
    TextField(
        value = query.value,
        onValueChange = {
            if (!it.contains("\n") && it.length < MAX_CHARS_LENGTH) {
                query.value = it
                viewModel.onSearchQueryUpdate(query = query.value)
            }
        },
        leadingIcon = {
            LeadingIcon(
                query = query,
                viewModel = viewModel,
                isSearching = isSearching
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester = focusRequester),
        trailingIcon = {
            if (query.value.isNotEmpty()) {
                TrailingIcon(
                    query = query,
                    viewModel = viewModel
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        singleLine = true,
        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.primary,
            cursorColor = Color.White,

            )
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun LeadingIcon(
    query: MutableState<String>,
    viewModel: CharactersListViewModel,
    isSearching: MutableState<Boolean>
) {
    IconButton(
        onClick = {
            query.value = ""
            isSearching.value = false
            viewModel.clearSearchQuery()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}

@Composable
fun TrailingIcon(
    query: MutableState<String>,
    viewModel: CharactersListViewModel
) {
    IconButton(
        onClick = {
            query.value = ""
            viewModel.clearSearchQuery()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Clear text"
        )
    }
}
