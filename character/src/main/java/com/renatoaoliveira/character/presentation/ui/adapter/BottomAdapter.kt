package com.renatoaoliveira.character.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.renatoaoliveira.character.R
import com.renatoaoliveira.character.presentation.ui.viewholder.BottomStatusViewHolder

class BottomAdapter : ListAdapter<BottomAdapter.BOTTOM_STATUS, BottomStatusViewHolder>(
    diffUtilCallback
) {

    private var currentStatus: BOTTOM_STATUS = BOTTOM_STATUS.EMPTY

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomStatusViewHolder {
        val layout = when (viewType) {
            BOTTOM_STATUS.LOADING.ordinal -> R.layout.bottom_status_load
            BOTTOM_STATUS.OFFLINE.ordinal -> R.layout.bottom_status_offline
            BOTTOM_STATUS.ERROR.ordinal -> R.layout.bottom_status_error
            else -> R.layout.bottom_status_empty
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return BottomStatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: BottomStatusViewHolder, position: Int) {
        // Do nothing
    }

    override fun getItemCount(): Int = if (currentStatus == BOTTOM_STATUS.EMPTY) 0 else 1

    override fun getItemViewType(position: Int): Int =
        currentStatus.ordinal

    fun setStatus(status: BOTTOM_STATUS) {
        currentStatus = status
        notifyDataSetChanged()
    }

    enum class BOTTOM_STATUS {
        EMPTY,
        LOADING,
        ERROR,
        OFFLINE
    }

    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<BOTTOM_STATUS>() {
            override fun areItemsTheSame(
                oldItem: BOTTOM_STATUS,
                newItem: BOTTOM_STATUS
            ): Boolean =
                false

            override fun areContentsTheSame(
                oldItem: BOTTOM_STATUS,
                newItem: BOTTOM_STATUS
            ): Boolean =
                false
        }
    }
}