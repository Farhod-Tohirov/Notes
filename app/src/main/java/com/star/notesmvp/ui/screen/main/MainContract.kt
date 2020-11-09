package com.star.notesmvp.ui.screen.main

import com.star.notesmvp.data.local.room.entity.NoteData

/**
 * Created by Farhod Tohirov on 09-Nov-20
 */

interface MainContract {
    interface Model {
        fun getNotes(block: (List<NoteData>) -> Unit)
        fun addNote(note: NoteData, block: (Long) -> Unit)
        fun deleteNote(note: NoteData, block: (Int) -> Unit)
        fun updateNote(note: NoteData, block: (NoteData) -> Unit)
        fun deleteAll(block: (Int) -> Unit)
    }

    interface View {
        fun loadViews()
        fun loadNotes(ls: List<NoteData>)
        fun openAddNoteFragment()
        fun openShowNoteFragment(note: NoteData)
        fun clearAdapter()
        fun addNewNote(note: NoteData)
        fun updateNote(note: NoteData)
        fun deleteNote(note: NoteData)
        fun hideLoading()
    }

    interface Presenter {
        fun init()
        fun addButtonClick()
        fun noteItemClick(note: NoteData)
        fun deleteAllClick()

        fun addNewNote(note: NoteData, block: (NoteData) -> Unit)
        fun updateNote(note: NoteData, block: (NoteData, NoteData) -> Unit)
        fun deleteNote(note: NoteData, block: (NoteData) -> Unit)
    }
}