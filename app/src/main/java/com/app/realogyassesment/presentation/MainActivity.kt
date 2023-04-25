package com.app.realogyassesment.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.WindowMetricsCalculator
import com.app.realogyassesment.common.Constants.CHARACTER_KEY
import com.app.realogyassesment.domain.model.CharacterObject
import com.app.realogyassesment.presentation.character_details.CharacterDetailScreen
import com.app.realogyassesment.presentation.characters_list.CharactersListScreen
import com.app.realogyassesment.presentation.ui.theme.RealogyAssesmentTheme
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint

enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealogyAssesmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screens.CharactersListScreen.route
                    ) {
                        composable(
                            route = Screens.CharactersListScreen.route
                        ) {
                            CharactersListScreen(navController, showOnePane = shouldUseSinglePane())
                        }
                        composable(
                            route = Screens.CharacterDetailScreen.route
                        ) { backStackEntry ->
                            val characterJson = backStackEntry.arguments?.getString(CHARACTER_KEY)
                            val moshi = Moshi.Builder().build()
                            val jsonAdapter = moshi.adapter(CharacterObject::class.java).lenient()
                            val characterObject = jsonAdapter.fromJson(characterJson)

                            CharacterDetailScreen(characterObject = characterObject)
                        }
                    }
                }
            }
        }
    }

    private fun shouldUseSinglePane(): Boolean {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)

        val widthDp = metrics.bounds.width() /
                resources.displayMetrics.density
        val widthWindowSizeClass = when {
            widthDp < 600f -> WindowSizeClass.COMPACT
            widthDp < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        val heightDp = metrics.bounds.height() /
                resources.displayMetrics.density
        val heightWindowSizeClass = when {
            heightDp < 480f -> WindowSizeClass.COMPACT
            heightDp < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        return widthWindowSizeClass != WindowSizeClass.EXPANDED &&
                heightWindowSizeClass != WindowSizeClass.EXPANDED
    }
}
