package com.example.autosnap.ui.translation

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.autosnap.ui.translation.recycler_adapter.TextToTranslate
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslationViewModel(private val app: Application) : AndroidViewModel(app) {
    val translatedLiveData: MutableLiveData<TextToTranslate> by lazy {
        MutableLiveData<TextToTranslate>()

    }

    suspend fun translate(text: TextToTranslate) {
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
                englishRussianTranslator.translate(text.originalText)
                    .addOnSuccessListener { translatedText ->
                        resultStr.append(translatedText)
                        translatedLiveData.value = TextToTranslate(
                            text.id,
                            text.originalText,
                            resultStr.toString(),
                            isTranslated = text.isTranslated
                        )
                        Log.e("vmin", translatedLiveData.value!!.isTranslated.toString())
                        Log.e("trans", translatedLiveData.value?.translatedText.toString())
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