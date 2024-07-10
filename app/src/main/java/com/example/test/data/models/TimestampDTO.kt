package com.example.test.data.models

import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

@Serializable
data class TimestampDTO(
    var year: Int = 0, // 2019
    var month: Int = 0, // 12
    var day: Int = 0, // 1
    var hour: Int = 0, // 21
    var minute: Int = 0, // 40
    var second: Int = 0, // 33
    var millisecond: Int = 0 // 0
) {
    private fun toCalendar(): GregorianCalendar {
        val cal = GregorianCalendar(year, month - 1, day, hour, minute, second)
        cal.set(Calendar.MILLISECOND, millisecond)
        return cal
    }

    constructor(dateTime: Calendar) : this(
        dateTime.get(Calendar.YEAR),
        dateTime.get(Calendar.MONTH) + 1,
        dateTime.get(Calendar.DAY_OF_MONTH),
        dateTime.get(Calendar.HOUR_OF_DAY),
        dateTime.get(Calendar.MINUTE),
        dateTime.get(Calendar.SECOND),
        dateTime.get(Calendar.MILLISECOND)
    )

    fun addDays(days: Long): TimestampDTO {
        val cal = toCalendar() as Calendar
        cal.add(Calendar.DAY_OF_MONTH, days.toInt())
        return TimestampDTO(cal)
    }

    fun addHours(hours: Long): TimestampDTO {
        val millis = toCalendar().time.time + (60L * 60L * 1000L * hours)
        val cal = GregorianCalendar()
        cal.timeInMillis = millis
        /*val cal = calendar.clone() as Calendar
        cal.add(Calendar.HOUR_OF_DAY, hours) */
        return TimestampDTO(cal)
    }

    fun subtractMillis(time: TimestampDTO): Long {
        return toCalendar().timeInMillis - time.toCalendar().timeInMillis
    }

    fun asCalendar(): Calendar {
        return toCalendar()
    }

    fun format(pattern: String): String {
        return SimpleDateFormat(pattern).format(toCalendar().time)
    }

    fun format(formatter: DateFormat): String {
        return formatter.format(asCalendar().time)
    }

    override fun toString(): String {
        return "TS(" + toIsoString() + ")"
    }

    fun printTime() = format(TIME_FORMAT)
    fun printDefault() = format(DEFAULT_FORMAT)

    fun toIsoString() = format(ISO_FORMAT)

    override fun equals(other: Any?): Boolean {
        return other is TimestampDTO &&
                other.year == year && other.month == month && other.day == day &&
                other.hour == hour && other.minute == minute && other.second == second && other.millisecond == millisecond
    }

    override fun hashCode(): Int {
        return (day + (month * 100) + (year * 10000))
            .xor(hour * 10000 + minute * 100 + second)
    }

    companion object {
        val ISO_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val DEFAULT_FORMAT = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
        val TIME_FORMAT = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

        fun now() =
            TimestampDTO(GregorianCalendar.getInstance())

        fun utcNow() = TimestampDTO(
            GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"))
        )

        fun fromMillis(value: Long): TimestampDTO {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = value
            return TimestampDTO(calendar)
        }

        fun parse(value: String, pattern: String): TimestampDTO {
            return TimestampDTO(
               Calendar.getInstance().apply {
                   time = SimpleDateFormat(pattern, Locale.ENGLISH).parse(value) ?: throw Exception("Invalid date")
               }
            )
        }
    }
}