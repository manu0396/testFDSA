package com.example.test.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    fun formatDateFromMillis(milliseconds: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return dateFormat.format(Date(milliseconds))
    }
}