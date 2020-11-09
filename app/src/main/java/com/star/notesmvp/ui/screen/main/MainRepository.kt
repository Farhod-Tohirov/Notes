package com.star.notesmvp.ui.screen.main

import android.os.Handler
import android.os.Looper
import com.star.notes.data.local.room.AppDatabase
import com.star.notesmvp.data.local.room.entity.NoteData
import java.util.concurrent.Executors

/**
 * Created by Farhod Tohirov on 09-Nov-20
 */

class MainRepository : MainContract.Model {

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private fun runOnWorkerThread(f: () -> Unit) {
        executor.execute { f() }
    }

    private fun runOnUIThread(f: () -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            f()
        } else {
            handler.post { f() }
        }
    }

    private val db = AppDatabase.getDatabase().noteDao()

    override fun getNotes(block: (List<NoteData>) -> Unit) {
        runOnWorkerThread {
            val ls = db.getAllNote()
            runOnUIThread { block(ls) }
        }
    }

    override fun addNote(note: NoteData, block: (Long) -> Unit) {
        runOnWorkerThread {
            val id = db.insert(note)
            runOnUIThread { block(id) }
        }
    }

    override fun deleteNote(note: NoteData, block: (Int) -> Unit) {
        runOnWorkerThread {
            val count = db.delete(note)
            runOnUIThread { block(count) }
        }
    }

    override fun updateNote(note: NoteData, block: (NoteData) -> Unit) {
        runOnWorkerThread {
            val count = db.update(note)
            val updated = db.getNote(note.id ?: 0L)
            runOnUIThread { block(updated) }
        }
    }

    override fun deleteAll(block: (Int) -> Unit) {
        runOnWorkerThread {
            val count = db.clearDB()
            runOnUIThread { block(count) }
        }
    }
}