package com.example.autosnap.ui.text_recognition

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.autosnap.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class TextRecognitionViewModel(private val app: Application) : AndroidViewModel(app) {
    var uri: Uri
    var currentUri: Uri? = null

    init {
        uri = initTempUri()
    }

    private fun initTempUri(): Uri {
        //gets the temp_images dir
        val tempImagesDir = File(
            app.applicationContext.filesDir, //this function gets the external cache dir
            app.getString(R.string.temp_images_dir)
        ) //gets the directory for the temporary images dir

        tempImagesDir.mkdir() //Create the temp_images dir
        //Creates the temp_image.jpg file
        val tempImage = File(
            tempImagesDir, //prefix the new abstract path with the temporary images dir path
            app.getString(R.string.temp_image)
        ) //gets the abstract temp_image file name

        //Returns the Uri object to be used with ActivityResultLauncher
        return FileProvider.getUriForFile(
            app.applicationContext,
            app.getString(R.string.authorities),
            tempImage
        )
    }

    val textLiveData: MutableLiveData<StringBuilder> by lazy {
        MutableLiveData<StringBuilder>()
    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    suspend fun defineText(image: InputImage) =
        withContext(viewModelScope.coroutineContext) {

            val textRecognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            launch {
                textRecognizer.process(image).addOnSuccessListener { task ->
                    val resultStr = StringBuilder()
                    resultStr.append(task.text)
                    textLiveData.setValue(resultStr)
                }
            }
        }
}