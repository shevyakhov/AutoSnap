package com.example.autosnap.ui.translation.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.autosnap.R
import com.example.autosnap.databinding.TranslationItemBinding

class TranslationAdapter(private val listener: TranslationAdapterListener) :
    ListAdapter<TextToTranslate, TranslationAdapter.TranslationViewHolder>(DIFF) {

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<TextToTranslate>() {
            override fun areItemsTheSame(
                oldItem: TextToTranslate,
                newItem: TextToTranslate
            ): Boolean {
                return oldItem.text == newItem.text
            }

            override fun areContentsTheSame(
                oldItem: TextToTranslate,
                newItem: TextToTranslate
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


    inner class TranslationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TranslationItemBinding.bind(itemView)
        private val nameObserver = Observer<Pair<Int, StringBuilder>> { translated ->
            if (getItem(adapterPosition).id == translated.first)
                binding.textView.text = translated.second
        }

        init {
            binding.translateBtn.setOnClickListener {
                listener.onItemClicked(getItem(adapterPosition), nameObserver)
            }
        }

        fun bind(binder: TextToTranslate) {
            binding.textView.text = binder.text
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        return TranslationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.translation_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface TranslationAdapterListener {
        fun onItemClicked(item: TextToTranslate, observer: Observer<Pair<Int, StringBuilder>>)
    }
}