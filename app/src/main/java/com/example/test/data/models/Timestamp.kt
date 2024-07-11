package com.example.test.data.models

class Timestamp(
    var millis: Long
) {
    companion object {
        fun now() = Timestamp(millisNow())
        private fun millisNow() = System.currentTimeMillis()
    }

    /**
     * Returns elapsed milliseconds since the marked time
     */
    fun elapsedTimeMS(): Long {
        return millisNow() - millis
    }

    /**
     * Returns elapsed seconds since the marked time
     */
    fun elapsedTimeS() = elapsedTimeMS() / 1000.toDouble()

    /**
     * Returns true if elapsed milliseconds since the marked time exceed the indicated amount.
     */
    fun olderThanMS(limitTimeMs: Long): Boolean {
        return elapsedTimeMS() > limitTimeMs
    }

    /**
     * Returns true if elapsed seconds since the marked time exceed the indicated amount.
     */
    fun olderThanS(limitTimeS: Long): Boolean {
        return olderThanMS(limitTimeS * 1000)
    }

    fun olderThan(otherTime: Timestamp): Boolean {
        return millis < otherTime.millis
    }

    override fun toString(): String {
        return "Timestamp($millis; elapsed_s=${elapsedTimeS()})"
    }
}