package com.markvtls.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*


fun String.getDayOfWeek(): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())
    val formattedDate = dateFormatter.parse(this)
    val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
    return formatter.format(formattedDate).replaceFirstChar { it.uppercase() }
}

fun String.getTime(): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormatter.parse(this)
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormatter.format(formattedDate)

}

fun String.getHourFromTime(): Float {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormatter.parse(this)
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = timeFormatter.format(formattedDate)
    return "${formattedTime[0]}${formattedTime[1]}".toFloat()
}
fun Boolean.yesOrNo(): String {
    return if (this) {
        "Есть"
    } else "Нет"
}


fun Float.checkHourValue(minValue: Float): Float {
    return if (this < minValue) 0.0F else this
 }