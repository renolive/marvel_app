package com.renatoaoliveira.common.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollLoader(
    private val positionThreshold: Int,
    private val onLoadMore: () -> Unit
) :
    RecyclerView.OnScrollListener() {

    private var loading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            val totalItemCount = layoutManager.getItemCount()

            if (!loading && lastVisibleItemPosition == totalItemCount - positionThreshold) {
                onLoadMore()
                loading = true
            }
        }
    }

    fun setNotLoading() {
        loading = false
    }
}
