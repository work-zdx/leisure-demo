package com.star.content

import java.text.SimpleDateFormat
import java.util.*


/**
 * 格式化时间
 */
fun formatDate(updateTime: String): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    try {
        val date = simpleDateFormat.parse(updateTime)

        if (date != null) {
            return compareTime(SimpleDateFormat("yyyy-MM-dd", Locale.CHINA), date.time)
        }
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
    return ""
}

/**
 * 计算时间间隔
 */
fun compareTime(formatter: SimpleDateFormat, time: Long): String {
    val interval = System.currentTimeMillis() - time

    if (interval < 0) {
        return "刚刚"
    }

    val day = interval / (24 * 60 * 60 * 1000)
    val hour = interval / (60 * 60 * 1000) - day * 24
    val minute = interval / (60 * 1000) - day * 24 * 60 - hour * 60

    return if (day > 0) {
        if (day in 1..7) {
            day.toString() + "天前"
        } else {
            formatter.format(time)
        }
    } else if (hour > 0) {
        hour.toString() + "小时前"
    } else if (minute > 0) {
        minute.toString() + "分钟前"
    } else {
        "刚刚"
    }
}

fun formatDuration(duration: Int): String {
    val hour = duration / (60 * 60)
    val minute = duration / 60 - hour * 60
    val second = duration % 60

    return when {
        hour > 0 -> {
            "${formatNumber(hour)}∶${formatNumber(minute)}∶${formatNumber(second)}"
        }
        else -> {
            "${formatNumber(minute)}∶${formatNumber(second)}"
        }
    }
}

fun formatNumber(number: Int): String {
    return if (number < 10) "0$number" else "$number"
}

