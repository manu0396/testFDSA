package com.example.test

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun firtsSmallPositive(){
        var response = 1
        val firtsInput = arrayOf(-1, -2, -3) // 1
        val secondInput = arrayOf(1,2,3) // 4
        val thirdsInput = arrayOf(1,3,4,1,6,2) // 5

        val sortArray = secondInput.sortedArray()
        secondInput.forEach {
            while (sortArray.contains(response)){
                response ++
            }
        }
        assert(response==4)
        assertEquals(response, 4)
    }
}