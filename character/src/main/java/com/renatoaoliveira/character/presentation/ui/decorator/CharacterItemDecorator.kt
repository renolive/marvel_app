package com.renatoaoliveira.character.presentation.ui.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.renatoaoliveira.character.R

class CharacterItemDecorator : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = view.context.resources.getDimension(R.dimen.pixel_8).toInt()
        outRect.set(offset, offset, offset, offset)
    }
}
