package com.example.autosnap.ui.translation.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.autosnap.R
import com.example.autosnap.databinding.TranslationItemBinding
import java.util.*

class TranslationAdapter : RecyclerView.Adapter<TranslationAdapter.TranslationViewHolder>() {

    private val list = ArrayList<TextToTranslate>()

    class TranslationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TranslationItemBinding.bind(itemView)
        fun bind(binder: TextToTranslate) {
            binding.textView.text = binder.text.uppercase(Locale.ROOT)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        return TranslationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.translation_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}