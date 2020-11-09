package com.star.notesmvp.ui.screen.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.star.notesmvp.ui.screen.adapter.MainListAdapter
import com.star.notesmvp.R
import com.star.notesmvp.data.local.room.entity.NoteData
import com.star.notesmvp.databinding.ActivityMainBinding
import com.star.notesmvp.ui.screen.add_note.AddNoteFragment
import com.star.notesmvp.utils.NoteDataDiffUtilCallback

class MainActivity : AppCompatActivity(), MainContract.View {

    lateinit var binding: ActivityMainBinding
    private val adapter = MainListAdapter()


    private val presenter by lazy { MainPresenter(this, MainRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.init()

    }

    override fun loadViews() {
        binding.list.adapter = adapter
        binding.list.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.mainToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.deleteAll -> {
                    AlertDialog.Builder(this)
                        .setTitle("Do you want to delete ALL?")
                        .setPositiveButton("YES") { dialog, _ ->
                            presenter.deleteAllClick()
                            dialog.dismiss()
                        }
                        .setNegativeButton("NO") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            }
            true
        }

        binding.addNote.setOnClickListener {
            presenter.addButtonClick()
        }

        adapter.setOnItemClickListener { noteData ->
            presenter.noteItemClick(noteData)
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(newText)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    override fun loadNotes(ls: List<NoteData>) {
        val noteDataDiffUtilCallback = NoteDataDiffUtilCallback(adapter.list, ls)
        val diffResult = DiffUtil.calculateDiff(noteDataDiffUtilCallback)

        adapter.submitNotesList(ls)
        diffResult.dispatchUpdatesTo(adapter)
    }

    override fun openAddNoteFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack("ADD_DATA")
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(binding.mainLayout.id, AddNoteFragment())
            .commit()
        binding.addNote.visibility = View.GONE
    }

    override fun openShowNoteFragment(note: NoteData) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("ADD_DATA")
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(binding.mainLayout.id, AddNoteFragment().apply { showNote(note) })
            .commit()
        binding.addNote.visibility = View.GONE
    }

    override fun clearAdapter() {
        val noteDataDiffUtilCallback = NoteDataDiffUtilCallback(adapter.list, ArrayList<NoteData>())
        val diffResult = DiffUtil.calculateDiff(noteDataDiffUtilCallback)

        adapter.submitNotesList(ArrayList<NoteData>())
        diffResult.dispatchUpdatesTo(adapter)
    }

    override fun addNewNote(note: NoteData) {
        presenter.addNewNote(note) { newNote ->
            val list = ArrayList(adapter.list)
            list.add(newNote)
            adapter.submitNotesList(list)
            adapter.notifyItemInserted(list.size - 1)
        }
    }

    override fun updateNote(note: NoteData) {
        presenter.updateNote(note) { oldNote, newNote ->
            val list = ArrayList(adapter.list)
            val index = list.indexOf(oldNote)
            list[index] = newNote
            adapter.submitNotesList(list)
            adapter.notifyItemChanged(index)
        }
    }

    override fun deleteNote(note: NoteData) {
        presenter.deleteNote(note) { deletedNote ->
            val list = ArrayList(adapter.list)
            val index = list.indexOf(deletedNote)
            list.removeAt(index)
            adapter.submitNotesList(list)
            adapter.notifyItemRemoved(index)
        }
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }
}