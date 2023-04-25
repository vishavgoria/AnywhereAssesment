package com.app.realogyassesment.presentation.character_details.component

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.realogyassesment.BuildConfig
import com.app.realogyassesment.domain.model.CharacterObject

@Composable
fun CharacterDetail(
    characterObject: CharacterObject,
    fontSize: Int = 18
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var url: String? = null
        if(!characterObject.imageUrl.isEmpty()){
            url = BuildConfig.BASE_WEB_URL + characterObject.imageUrl
        }
        url?.let { Log.d("Image_URL", it) }
        AsyncImage(
            model = url,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
//            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
//            error = painterResource(id = R.drawable.ic_error_img)
        )

        Text(
            text = characterObject.description.replace('+', ' '),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontSize = fontSize.sp
        )

    }


}