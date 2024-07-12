package com.example.test.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatDateFromMillis(milliseconds: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return dateFormat.format(Date(milliseconds))
    }

    fun extractMilliseconds(timestampString: String): Long {
        val startIndex = timestampString.indexOf('(') + 1
        val endIndex = timestampString.indexOf(',', startIndex) // Search for comma after '('

        if (startIndex in 0..<endIndex) {
            val millisSubstring = timestampString.substring(startIndex, endIndex)
            return millisSubstring.toLongOrNull() ?: 0L
        } else {
            return 0L // Handle cases where the format is unexpected
        }
    }
}