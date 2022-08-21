package com.example.autosnap.ui.translation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autosnap.databinding.FragmentTranslationBinding
import com.example.autosnap.ui.translation.recycler_adapter.TextToTranslate
import com.example.autosnap.ui.translation.recycler_adapter.TranslationAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TranslationFragment : Fragment() {

    private var _binding: FragmentTranslationBinding? = null
    private val binding get() = _binding!!
    private val adapter =
        TranslationAdapter(object : TranslationAdapter.TranslationAdapterListener {
            override fun onItemClicked(
                item: TextToTranslate,
                observer: Observer<TextToTranslate>
            ) {
                viewModel.translatedLiveData.observe(viewLifecycleOwner, observer)

                viewModel.apply {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.translate(item)
                    }
                }
            }
        })

    private val viewModel by viewModels<TranslationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchBar.setOnClickListener {
            binding.searchBar.onActionViewExpanded()
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter.submitList(
            arrayListOf(
                TextToTranslate(0, "some text to translate\nfor you"),
                TextToTranslate(1, "based department"),
                TextToTranslate(2, "crippling depression"),
                TextToTranslate(3, "mouse crawling"),
                TextToTranslate(4, "reject humanity become pussy"),
                TextToTranslate(5, "youth\nis\nending")
            )
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

