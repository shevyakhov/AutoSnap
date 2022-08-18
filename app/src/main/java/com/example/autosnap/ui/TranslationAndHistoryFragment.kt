package com.example.autosnap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.autosnap.databinding.FragmentTranslationAndHistoryBinding

class TranslationAndHistoryFragment : Fragment() {

    private var _binding: FragmentTranslationAndHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TranslationAndHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslationAndHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TranslationAndHistoryViewModel::class.java]
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

