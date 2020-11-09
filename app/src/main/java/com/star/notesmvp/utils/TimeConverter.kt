package com.star.notesmvp.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Farhod Tohirov on 09-Nov-20
 */

object TimeConverter{
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MMM.yyyy, HH:mm")
        return format.format(date)
    }

}