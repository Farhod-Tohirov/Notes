package com.star.notesmvp.ui.screen.main

import com.star.notesmvp.data.local.room.entity.NoteData

/**
 * Created by Farhod Tohirov on 09-Nov-20
 */

class MainPresenter(private val view: MainContract.View, private val model: MainContract.Model) :
    MainContract.Presenter {
    override fun init() {
        view.loadViews()
        model.getNotes {
            view.loadNotes(it)
        }
    }

    override fun addButtonClick() {
        view.openAddNoteFragment()
    }

    override fun noteItemClick(note: NoteData) {
        view.openShowNoteFragment(note)
    }

    override fun deleteAllClick() {
        model.deleteAll {
            view.clearAdapter()
        }
    }

    override fun addNewNote(note: NoteData, block: (NoteData) -> Unit) {
        val newNote = note.copy()
        model.addNote(note) {
            if (it > 0) {
                newNote.id = it
                block(newNote)
            }
        }
    }

    override fun updateNote(note: NoteData, block: (NoteData, NoteData) -> Unit) {
        val newNote = note.copy()
        model.updateNote(note) { newData ->
            block(note, newData)
        }
    }

    override fun deleteNote(note: NoteData, block: (NoteData) -> Unit) {
        model.deleteNote(note) {
            if (it > 0) {
                model.getNotes {
                    view.loadNotes(it)
                }
            }
        }
    }
}