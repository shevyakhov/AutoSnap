package com.example.autosnap.ui.translation

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslationViewModel(private val app: Application) : AndroidViewModel(app) {
    val translatedLiveData: MutableLiveData<Pair<Int, StringBuilder>> by lazy {
        MutableLiveData<Pair<Int, StringBuilder>>()

    }

    suspend fun translate(text: String, id:Int) {
        withContext(viewModelScope.coroutineContext) {

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.RUSSIAN)
                .build()
            val englishRussianTranslator = Translation.getClient(options)
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            launch {
                val resultStr = StringBuilder()

                englishRussianTranslator.downloadModelIfNeeded(conditions)
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            app.applicationContext,
                            exception.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                englishRussianTranslator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        resultStr.append(translatedText)
                        translatedLiveData.setValue(id to resultStr)
                        Log.e("trans",translatedLiveData.value?.second.toString())
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            app.applicationContext,
                            "no valid translation",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
            }
        }
    }

}