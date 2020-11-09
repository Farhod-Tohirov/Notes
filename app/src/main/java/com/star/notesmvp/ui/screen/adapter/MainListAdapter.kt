package com.star.notesmvp.ui.screen.adapter

import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.star.notesmvp.R
import com.star.notesmvp.data.local.room.entity.NoteData
import com.star.notesmvp.databinding.NoteItemBinding
import com.star.notesmvp.utils.NoteDataDiffUtilCallback
import com.star.notesmvp.utils.SingleBlock
import com.star.notesmvp.utils.TimeConverter
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Farhod Tohirov on 08-Nov-20
 */

class MainListAdapter : RecyclerView.Adapter<MainListAdapter.ViewHolder>(), Filterable {

    private var itemListener: SingleBlock<NoteData>? = null
    private var notesListFull = ArrayList<NoteData>()
    var list = ArrayList<NoteData>()

    companion object {
        var DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<NoteData>() {
            override fun areItemsTheSame(oldItem: NoteData, newItem: NoteData) =
                newItem.id == oldItem.id

            override fun areContentsTheSame(oldItem: NoteData, newItem: NoteData) =
                newItem.title == oldItem.title && newItem.description == oldItem.description && newItem.savedDate == oldItem.savedDate &&
                        newItem.editedDate == oldItem.editedDate && newItem.images.hashCode() == oldItem.images.hashCode()
        }
    }

    inner class ViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.item.setOnClickListener { itemListener?.invoke(list[adapterPosition]) }
        }

        fun bind() {
            val data = list[adapterPosition]

            val THUMBSIZE = 256
            var path = ""
            data.images.forEach { image ->
                if (path == "") {
                    path = image
                    return@forEach
                }
            }

            if (path != "") {
                val thumbImage = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(path),
                    THUMBSIZE, THUMBSIZE
                )
                Glide.with(binding.image).load(thumbImage).centerCrop()
                    .placeholder(R.drawable.ic_picture).into(binding.image)
            } else {
                binding.image.visibility = View.GONE
            }

            binding.title.text = data.title
            binding.subTitle.text = data.description
            val savedDate = TimeConverter.convertLongToTime(data.savedDate)
            val editDate =
                if (data.editedDate != 0L) TimeConverter.convertLongToTime(data.editedDate) else ""
            var date = "Saved: $savedDate"
            if (editDate != "") date += "\nEdited: $editDate"
            binding.date.text = date

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        NoteItemBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    fun setOnItemClickListener(f: SingleBlock<NoteData>) {
        itemListener = f
    }

    fun submitNotesList(list: List<NoteData>, fromActivity: Boolean = true) {
        this.list.clear()
        this.list.addAll(list)
        if (fromActivity)
            notesListFull = ArrayList(list)
    }

    override fun getFilter(): Filter = filter

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<NoteData>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(notesListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                for (item in notesListFull) {
                    if (item.title.toLowerCase(Locale.getDefault())
                            .contains(filterPattern) || item.description.toLowerCase(Locale.getDefault())
                            .contains(filterPattern)
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(p0: CharSequence, results: FilterResults) {
            val ls = ArrayList(list)
            ls.clear()
            ls.addAll(results.values as ArrayList<NoteData>)
            val noteDataDiffUtilCallback = NoteDataDiffUtilCallback(list, ls)
            val diffResult = DiffUtil.calculateDiff(noteDataDiffUtilCallback)

            submitNotesList(ls, false)
            diffResult.dispatchUpdatesTo(this@MainListAdapter)
        }
    }

    override fun getItemCount() = list.size
}