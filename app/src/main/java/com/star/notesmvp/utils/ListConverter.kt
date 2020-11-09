package com.star.notesmvp.utils

import androidx.room.TypeConverter

/**
 * Created by Farhod Tohirov on 08-Nov-20
 */

class ListConverter {
    @TypeConverter
    fun fromString(stringListString: String?): List<String>? {
        return stringListString?.split(",")?.map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>?): String? {
        return stringList?.joinToString(separator = ",")
    }
}