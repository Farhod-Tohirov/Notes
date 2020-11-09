package com.star.notesmvp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.star.notesmvp.data.local.room.entity.NoteData

/**
 * Created by Farhod Tohirov on 08-Nov-20
 */

@Dao
interface NoteDao: BaseDao<NoteData> {

    @Query("SELECT * FROM NoteData")
    fun getAllNote(): List<NoteData>

    @Query("DELETE FROM NoteData")
    fun clearDB(): Int

    @Query("SELECT * FROM NoteData WHERE id=:noteId")
    fun getNote(noteId: Long): NoteData
}
