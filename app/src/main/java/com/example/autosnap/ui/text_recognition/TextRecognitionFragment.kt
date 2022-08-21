package com.example.autosnap.ui.text_recognition

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.autosnap.R
import com.example.autosnap.databinding.FragmentTextRecognitionBinding
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class TextRecognitionFragment : Fragment() {
    private var _binding: FragmentTextRecognitionBinding? = null
    private val viewModel by viewModels<TextRecognitionViewModel>()
    private lateinit var tempImageUri: Uri

    private val binding get() = _binding!!
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var pickPictureLauncher: ActivityResultLauncher<String>
    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                takePictureLauncher.launch(tempImageUri)

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

        tempImageUri = viewModel.uri
        registerTakePictureLauncher(tempImageUri)
        registerPickPictureLauncher()
        val nameObserver = Observer<StringBuilder> { newName ->
            binding.text.text = newName
        }
        viewModel.textLiveData.observe(viewLifecycleOwner, nameObserver)
        binding.cameraBtn.setOnClickListener {
            takePhoto()
        }
        binding.galleryBtn.setOnClickListener {
            pickPhoto()
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
                takePictureLauncher.launch(viewModel.uri)
            } else {
                permReqLauncher.launch(
                    PERMISSIONS
                )
            }
        }
    }

    private fun pickPhoto() {
        activity?.let {
            if (viewModel.hasPermissions(activity as Context, PERMISSIONS)) {
                pickPictureLauncher.launch(getString(R.string.image_intent))
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


    private fun registerTakePictureLauncher(path: Uri) {

        //Creates the ActivityResultLauncher
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.image.setImageURI(null) //rough handling of image changes. Real code need to handle different API levels.
                binding.image.setImageURI(path)
                defineBuild(path)
            }
        }
    }

    private fun registerPickPictureLauncher() {
        //Creates the ActivityResultLauncher
        pickPictureLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                binding.image.setImageURI(it)
                defineBuild(it)
            }
        }
    }

    private fun defineBuild(path: Uri) {
        val image: InputImage
        try {
            image = InputImage.fromFilePath(requireContext(), path)
            CoroutineScope(Dispatchers.Default).launch {
                viewModel.defineText(image)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.background = requireActivity().getDrawable(R.color.md_theme_light_background)
    }
}
