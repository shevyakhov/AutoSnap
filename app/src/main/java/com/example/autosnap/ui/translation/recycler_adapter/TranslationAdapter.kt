package com.example.autosnap.ui.translation.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.autosnap.R
import com.example.autosnap.databinding.TranslationItemBinding

class TranslationAdapter(private val listener: TranslationAdapterListener) :
    ListAdapter<TextToTranslate, TranslationAdapter.TranslationViewHolder>(DIFF), Filterable {
    private var originalList: List<TextToTranslate> = currentList.toList()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty())
                        originalList
                    else
                        onFilter(originalList, constraint.toString())
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<TextToTranslate>)
            }
        }
    }

    private fun onFilter(
        originalList: List<TextToTranslate>,
        string: String
    ): List<TextToTranslate> {
        return originalList.filter { it.originalText.lowercase().contains(string) }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<TextToTranslate>() {
            override fun areItemsTheSame(
                oldItem: TextToTranslate,
                newItem: TextToTranslate
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TextToTranslate,
                newItem: TextToTranslate
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


    fun submitOriginalList(list: List<TextToTranslate>) {
        originalList = list
        submitList(list)
    }


    inner class TranslationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TranslationItemBinding.bind(itemView)
        private val nameObserver = Observer<TextToTranslate> { textToTranslate ->

            if (adapterPosition != -1) {
                if (getItem(adapterPosition).id == textToTranslate.id)
                    bindUpdate(textToTranslate, adapterPosition)
            }
        }

        private fun bindUpdate(textToTranslate: TextToTranslate, adapterPosition: Int) {
            if (getItem(adapterPosition).id == textToTranslate.id) {
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
                val position = getItem(adapterPosition)
                listener.onItemClicked(position, nameObserver)
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