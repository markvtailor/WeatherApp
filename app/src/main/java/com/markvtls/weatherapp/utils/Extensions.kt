package com.markvtls.weatherapp.utils

import com.markvtls.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*


fun String.getDayOfWeek(): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())
    val formattedDate = dateFormatter.parse(this)
    val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
    return formatter.format(formattedDate).translateDayOfWeek().replaceFirstChar { it.uppercase() }
}
fun String.getShortDayOfWeek(): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())
    val formattedDate = dateFormatter.parse(this)
    val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
    return when(formatter.format(formattedDate).translateDayOfWeek().replaceFirstChar { it.uppercase() }) {
        "Понедельник" -> "Пн"
        "Вторник" -> "Вт"
        "Среда" -> "Ср"
        "Четверг" -> "Чт"
        "Пятница" -> "Пт"
        "Суббота" -> "Сб"
        "Воскресенье" -> "Вс"
        else -> "?"
    }
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

fun String.formatDate(): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormatter.parse(this)
    val formatter = SimpleDateFormat("dd.MM", Locale.getDefault())
    return formatter.format(formattedDate)
}
fun Boolean.yesOrNo(): String {
    return if (this) {
        "Есть"
    } else "Нет"
}


fun Float.checkHourValue(minValue: Float): Float {
    return if (this < minValue) 0.0F else this
 }

fun String.chooseDegreesUnit(): String {
    return if (this == "F") "\u2109" else "\u2103"
}

fun String.translateSpeedUnit(): String {
    return if (this == "mi/h") "миль/ч" else "км/ч"
}
fun String.translateDayOfWeek(): String {
    println(this)
    return when(this) {
        "Monday" -> "Понедельник"
        "Tuesday" -> "Вторник"
        "Wednesday" -> "Среда"
        "Thursday" -> "Четверг"
        "Friday" -> "Пятница"
        "Saturday" -> "Суббота"
        "Sunday" -> "Воскресенье"
        else -> this
    }
}
fun Int.chooseIcon(): Int {
    return when(this) {
        1 -> R.drawable.weather_1
        2 -> R.drawable.weather_2
        3 -> R.drawable.weather_3
        4 -> R.drawable.weather_4
        5 -> R.drawable.weather_5
        6 -> R.drawable.weather_6
        7 -> R.drawable.weather_7
        8 -> R.drawable.weather_8
        11 -> R.drawable.weather_11
        12 -> R.drawable.weather_12
        13 -> R.drawable.weather_13
        14 -> R.drawable.weather_14
        15 -> R.drawable.weather_15
        17 -> R.drawable.weather_17
        18 -> R.drawable.weather_18
        19 -> R.drawable.weather_19
        20 -> R.drawable.weather_20
        21 -> R.drawable.weather_21
        22 -> R.drawable.weather_22
        23 -> R.drawable.weather_23
        24 -> R.drawable.weather_24
        25 -> R.drawable.weather_25
        26 -> R.drawable.weather_26
        29 -> R.drawable.weather_29
        30 -> R.drawable.weather_30
        31 -> R.drawable.weather_31
        32 -> R.drawable.weather_32
        33 -> R.drawable.weather_33
        34 -> R.drawable.weather_34
        35 -> R.drawable.weather_35
        36 -> R.drawable.weather_36
        37 -> R.drawable.weather_37
        38 -> R.drawable.weather_38
        39 -> R.drawable.weather_39
        40 -> R.drawable.weather_40
        41 -> R.drawable.weather_41
        42 -> R.drawable.weather_42
        43 -> R.drawable.weather_43
        44 -> R.drawable.weather_44
        else -> R.drawable.weather_1
    }
}