package com.star.notesmvp.utils

import androidx.recyclerview.widget.DiffUtil
import com.star.notesmvp.data.local.room.entity.NoteData


/**
 * Created by Farhod Tohirov on 09-Nov-20
 */

class NoteDataDiffUtilCallback(private val oldList: List<NoteData>, private val newList: List<NoteData>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNoteData: NoteData = oldList[oldItemPosition]
        val newNoteData: NoteData = newList[newItemPosition]
        return oldNoteData.id === newNoteData.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNoteData: NoteData = oldList[oldItemPosition]
        val newNoteData: NoteData = newList[newItemPosition]
        return (oldNoteData.title == newNoteData.title
                && oldNoteData.description == newNoteData.description && oldNoteData.savedDate == newNoteData.savedDate
                && oldNoteData.editedDate == newNoteData.editedDate && oldNoteData.images.hashCode()== newNoteData.images.hashCode())
    }

}