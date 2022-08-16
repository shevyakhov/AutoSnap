package com.example.autosnap.ui

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.autosnap.R
import com.example.autosnap.databinding.FragmentTextRecognitionBinding
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


class TextRecognitionFragment : Fragment() {
    private var _binding: FragmentTextRecognitionBinding? = null
    private lateinit var viewModel: TextRecognitionViewModel
    private lateinit var tempImageUri: Uri
    private val binding get() = _binding!!
    private lateinit var resultLauncher: ActivityResultLauncher<Uri>
    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                resultLauncher.launch(initTempUri())

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextRecognitionBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TextRecognitionViewModel::class.java]
        tempImageUri = initTempUri()
        registerTakePictureLauncher(tempImageUri)

        val nameObserver = Observer<StringBuilder> { newName ->
            binding.text.text = newName
        }
        viewModel.translatedLiveData.observe(viewLifecycleOwner, nameObserver)
        binding.button.setOnClickListener {
            takePhoto()
        }
    }


    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }


    private fun takePhoto() {
        activity?.let {
            if (viewModel.hasPermissions(activity as Context, PERMISSIONS)) {
                resultLauncher.launch(initTempUri())
            } else {
                permReqLauncher.launch(
                    PERMISSIONS
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initTempUri(): Uri {
        //gets the temp_images dir
        val tempImagesDir = File(
            requireActivity().applicationContext.filesDir, //this function gets the external cache dir
            getString(R.string.temp_images_dir)
        ) //gets the directory for the temporary images dir

        tempImagesDir.mkdir() //Create the temp_images dir

        //Creates the temp_image.jpg file
        val tempImage = File(
            tempImagesDir, //prefix the new abstract path with the temporary images dir path
            getString(R.string.temp_image)
        ) //gets the abstract temp_image file name

        //Returns the Uri object to be used with ActivityResultLauncher
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            getString(R.string.authorities),
            tempImage
        )
    }

    private fun registerTakePictureLauncher(path: Uri) {

        //Creates the ActivityResultLauncher
        resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            binding.image.setImageURI(null) //rough handling of image changes. Real code need to handle different API levels.
            binding.image.setImageURI(path)
            val image: InputImage
            try {
                image = InputImage.fromFilePath(requireContext(), path)
                CoroutineScope(Dispatchers.Default).launch {
                    translate(image)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private suspend fun translate(image: InputImage) {
        viewModel.translateText(image)
    }
}
