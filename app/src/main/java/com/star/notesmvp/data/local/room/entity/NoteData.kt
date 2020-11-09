package com.star.notesmvp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Farhod Tohirov on 08-Nov-20
 */

@Entity
data class NoteData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var title: String ="",
    var description: String = "",
    var images: List<String>,
    var savedDate: Long = 0L,
    var editedDate: Long = 0L
)