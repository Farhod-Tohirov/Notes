package com.star.notesmvp.ui.screen.add_note

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.star.notes.app.App
import com.star.notesmvp.R
import com.star.notesmvp.data.local.room.entity.NoteData
import com.star.notesmvp.databinding.FragmentAddNoteBinding
import com.star.notesmvp.ui.screen.adapter.ImageAdapter
import com.star.notesmvp.ui.screen.main.MainActivity
import com.star.notesmvp.utils.IMAGE_PICK
import com.star.notesmvp.utils.PathUtil
import com.star.notesmvp.utils.TimeConverter
import com.star.notesmvp.utils.checkPermission
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Farhod Tohirov on 08-Nov-20
 */

class AddNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddNoteBinding
    private val adapter = ImageAdapter()
    private var ls = ArrayList<String>()
    private var noteData: NoteData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menu = binding.toolbar.menu

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        binding.toolbar.setNavigationOnClickListener {
            finishFragment()
        }

        adapter.setOnImageClickListener { position ->
            AlertDialog.Builder(context ?: App.instance)
                .setTitle("Do you want to delete?")
                .setPositiveButton("YES") { dialog, _ ->
                    ls.removeAt(position)
                    adapter.submitList(ls.toMutableList())
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addImage -> openImagePicker()
                R.id.deleteNote -> deleteNote()
            }
            true
        }

        menu.findItem(R.id.done).setOnMenuItemClickListener {
            addNoteDataOrNot()
            true
        }
        if (noteData != null) {
            binding.title.setText(noteData!!.title)
            binding.description.setText(noteData!!.description)
            adapter.submitList(noteData!!.images.toMutableList())
            var date = "Saved on: ${TimeConverter.convertLongToTime(noteData!!.savedDate)}"
            if (noteData?.editedDate != 0L) date += " | Edit on: ${
                TimeConverter.convertLongToTime(
                    noteData!!.editedDate
                )
            }"
            binding.date.text = date
        } else {
            val date = TimeConverter.convertLongToTime(Date().time)
            binding.date.text = date
        }
    }

    private fun openImagePicker() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, IMAGE_PICK)
            }
        }
    }

    private fun deleteNote() {
        if (noteData != null) {
            androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setTitle("Do you want to delete this image?")
                .setPositiveButton("YES") { dialog, _ ->
                    (activity as MainActivity).deleteNote(noteData!!)
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
        finishFragment()
    }

    private fun addNoteDataOrNot() {
        val title = binding.title.text.toString()
        val description = binding.description.text.toString()
        val timeNow = Date().time

        if (noteData == null) {
            if ((ls.size == 1) && (ls[0] == "")) ls.clear()
            val note =
                NoteData(title = title, description = description, images = ls, savedDate = timeNow)
            (activity as MainActivity).addNewNote(note)
        } else {
            if (title != noteData?.title || description != noteData?.description) {
                noteData?.title = title
                noteData?.description = description
                noteData?.images = ls
                noteData?.editedDate = timeNow
                (activity as MainActivity).updateNote(noteData!!)
            } else {
                if (ls.size != noteData?.images?.size) {
                    noteData?.editedDate = timeNow
                    noteData?.images = ls
                    (activity as MainActivity).updateNote(noteData!!)
                } else
                    ls.forEachIndexed { index, image ->
                        if (noteData?.images?.get(index) != image) {
                            noteData?.editedDate = timeNow
                            noteData?.images = ls
                            (activity as MainActivity).updateNote(noteData!!)
                            return@forEachIndexed
                        }
                    }
            }
        }
        finishFragment()
    }

    private fun finishFragment() {
        (activity as MainActivity).onBackPressed()
    }

    fun showNote(noteData: NoteData) {
        this.noteData = noteData
        this.ls = if (noteData.images.isNotEmpty()) ArrayList(noteData.images) else ls
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.addNote.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            val path = PathUtil.getPath(context, uri)
            ls.add(path)
            adapter.submitList(ls.toMutableList())
        }
    }
}