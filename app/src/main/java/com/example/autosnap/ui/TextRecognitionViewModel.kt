package com.example.autosnap.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognitionViewModel : ViewModel() {
    fun cameraIntent() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    fun translateText(image: InputImage): StringBuilder {
        val res = StringBuilder()
        val textRecognizer =
                    TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                textRecognizer.process(image).addOnSuccessListener {
                    res.append(it)
                    Log.e("____________", it.text)
                }
        return res
    }
}