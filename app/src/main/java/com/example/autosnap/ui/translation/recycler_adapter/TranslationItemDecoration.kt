package com.example.autosnap.ui.translation.recycler_adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TranslationItemDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }
}