package com.app.realogyassesment.data.remote.dto

import com.app.realogyassesment.domain.model.CharacterObject

data class RelatedTopic(
    val FirstURL: String,
    val Icon: Icon,
    val Result: String,
    val Text: String
) {
    fun getName(): String {
        val name = StringBuilder()

        for(ch in Text) {
            if(ch != '-') {
                name.append(ch)
            } else {
                break
            }
        }

        return name.toString()
    }
}

fun RelatedTopic.toCharacter(): CharacterObject {
    return CharacterObject(
        imageUrl = Icon.URL,
        name = getName(),
        description = Text
    )
}
