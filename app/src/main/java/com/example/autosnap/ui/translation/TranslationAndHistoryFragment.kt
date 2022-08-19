package com.example.autosnap.ui.translation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autosnap.databinding.FragmentTranslationAndHistoryBinding
import com.example.autosnap.ui.translation.recycler_adapter.TextToTranslate
import com.example.autosnap.ui.translation.recycler_adapter.TranslationAdapter

class TranslationAndHistoryFragment : Fragment() {

    private var _binding: FragmentTranslationAndHistoryBinding? = null
    private val binding get() = _binding!!
    private val adapter =
        TranslationAdapter(object : TranslationAdapter.TranslationAdapterListener {
            override fun onItemClicked(item: TextToTranslate) {
                Toast.makeText(requireContext(), "translate me", Toast.LENGTH_SHORT).show()
                viewModel.apply {

                }
            }
        })

    private val viewModel by viewModels<TranslationAndHistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslationAndHistoryBinding.inflate(inflater, container, false)
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
                TextToTranslate("some text\nsome text"),
                TextToTranslate("some text"),
                TextToTranslate("some text"),
                TextToTranslate("some text"),
                TextToTranslate("some text"),
                TextToTranslate("blablabla\nsome text\nsome text")
            )
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

