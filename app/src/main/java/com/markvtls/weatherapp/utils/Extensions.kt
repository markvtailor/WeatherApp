package com.markvtls.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.hours


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
    val formattedTime = timeFormatter.format(formattedDate)
    println(formattedTime)
    return formattedTime

}

fun String.getHourFromTime(): Float {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormatter.parse(this)
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = timeFormatter.format(formattedDate)
    val hour = "${formattedTime[0]}${formattedTime[1]}".toFloat()
    println(hour)
    return hour
}
fun Boolean.yesOrNo(): String {
    return if (this) {
        "Есть"
    } else "Нет"
}