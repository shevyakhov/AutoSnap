package com.example.autosnap.ui

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TextRecognitionViewModel : ViewModel() {

    val translatedLiveData: MutableLiveData<StringBuilder> by lazy {
        MutableLiveData<StringBuilder>()
    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    suspend fun translateText(image: InputImage) =
        withContext(viewModelScope.coroutineContext) {

            var r: List<String> = listOf()
            val textRecognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val res = launch {
                val process = textRecognizer.process(image).addOnCompleteListener { task ->
                    val resultStr = StringBuilder()
                    resultStr.append(task.result.text)
                    translatedLiveData.setValue(resultStr)
                }
            }
        }
}