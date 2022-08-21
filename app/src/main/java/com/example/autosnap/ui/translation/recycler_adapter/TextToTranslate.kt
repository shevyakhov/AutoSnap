package com.example.autosnap.ui.translation.recycler_adapter

data class TextToTranslate(
    val id: Int,
    val originalText: String,
    var translatedText: String = "",
    var isTranslated: Boolean = false
)
