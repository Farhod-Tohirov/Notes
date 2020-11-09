package com.star.notesmvp.ui.screen.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.star.notesmvp.R
import com.star.notesmvp.databinding.ImageItemBinding
import com.star.notesmvp.utils.SingleBlock

/**
 * Created by Farhod Tohirov on 08-Nov-20
 */

class ImageAdapter : ListAdapter<String, ImageAdapter.ViewHolder>(ITEM_CALLBACK) {

    private var listenerImageClick: SingleBlock<Int>? = null

    companion object {
        var ITEM_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ImageItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val data = getItem(adapterPosition)
            if (data != "")
                Glide.with(binding.image).load(BitmapFactory.decodeFile(data)).centerCrop()
                    .placeholder(R.drawable.ic_picture).into(binding.image)
            binding.image.setOnClickListener { listenerImageClick?.invoke(adapterPosition) }
        }
    }

    fun setOnImageClickListener(f: SingleBlock<Int>) {
        listenerImageClick = f
    }
}