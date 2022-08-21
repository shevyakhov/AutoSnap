package com.example.autosnap.ui.translation.recycler_adapter

import android.util.Log
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
                return oldItem.originalText == newItem.originalText
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
        private val nameObserver = Observer<TextToTranslate> { textToTranslate ->

            if (getItem(adapterPosition).id == textToTranslate.id) {
                Log.e("is", textToTranslate.isTranslated.toString())
                if (!textToTranslate.isTranslated) {
                    binding.textView.text = textToTranslate.translatedText
                    binding.translateBtn.setImageResource(R.drawable.back_ic)
                    getItem(adapterPosition).isTranslated = true
                } else {
                    binding.textView.text = textToTranslate.originalText
                    binding.translateBtn.setImageResource(R.drawable.translate_ic)
                    getItem(adapterPosition).isTranslated = false
                }
            }
        }

        init {
            binding.translateBtn.setOnClickListener {
                listener.onItemClicked(getItem(adapterPosition), nameObserver)
            }
        }

        fun bind(binder: TextToTranslate) {
            binding.textView.text = binder.originalText
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
        fun onItemClicked(item: TextToTranslate, observer: Observer<TextToTranslate>)
    }
}