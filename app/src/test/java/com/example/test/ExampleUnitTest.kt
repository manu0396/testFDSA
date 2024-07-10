package com.example.test

import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun firtsSmallPositive() {
        var response = 1
        val firtsInput = arrayOf(-1, -2, -3) // 1
        val secondInput = arrayOf(1, 2, 3) // 4
        val thirdsInput = arrayOf(1, 3, 4, 1, 6, 2) // 5

        val sortArray = secondInput.sortedArray()
        secondInput.forEach {
            if (it > 0) {
                while (sortArray.contains(response)) {
                    response++
                }
            }
        }

        assert(response == 4)
        assertEquals(response, 4)
    }

    @Test
    fun testFindCombinations() {
        val nums = listOf(1, 5, 3, 2)
        val target = 6
        val expectedCombinations = listOf(
            listOf(1, 5),
            listOf(1, 3, 2)
        )

        val actualCombinations = findCombinations(nums, target)

        assertEquals(expectedCombinations.size, actualCombinations.size)

        for (expectedCombination in expectedCombinations) {
            assert(actualCombinations.contains(expectedCombination)) { "Combinación esperada no encontrada: $expectedCombination" }
        }
    }

    fun findCombinations(nums: List<Int>, target: Int): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        findCombinationsHelper(nums, target, 0, mutableListOf(), result)
        return result
    }

    private fun findCombinationsHelper(
        nums: List<Int>,
        target: Int,
        start: Int,
        current: MutableList<Int>,
        result: MutableList<List<Int>>,
    ) {
        if (target == 0) {
            result.add(ArrayList(current)) // Se agrega una copia de la combinación actual
            return
        }

        for (i in start until nums.size) {
            if (nums[i] > target) {
                continue
            }

            current.add(nums[i])
            findCombinationsHelper(nums, target - nums[i], i + 1, current, result)
            current.removeAt(current.size - 1)
        }
    }


    data class ObjectData(val position: Pair<Double, Double>, val velocity: Pair<Double, Double>)


    fun calculateMeetingPoint(object1: ObjectData, object2: ObjectData): Pair<Double, Double>? {
        // Calcular la velocidad relativa entre los dos objetos
        val relativeVelocity = Pair(object2.velocity.first - object1.velocity.first, object2.velocity.second - object1.velocity.second)

        // Verificar si los objetos son paralelos y nunca se cruzarán
        if (relativeVelocity.first == 0.0 && relativeVelocity.second == 0.0) {
            return null
        }

        // Calcular el tiempo que tardarán en encontrarse utilizando la fórmula del movimiento uniforme
        val timeToMeet = if (relativeVelocity.first == 0.0) {
            // Si la velocidad en la dirección x es cero, usar la posición y relativa y la velocidad y relativa
            (object1.position.second - object2.position.second) / relativeVelocity.second
        } else {
            // Si la velocidad en la dirección x no es cero, usar la posición x relativa y la velocidad x relativa
            (object1.position.first - object2.position.first) / relativeVelocity.first
        }

        // Calcular las coordenadas del punto de encuentro utilizando el tiempo calculado
        return Pair(
            object2.position.first + object2.velocity.first * timeToMeet,
            object2.position.second + object2.velocity.second * timeToMeet
        )
    }

    @Test
    fun testObjectsMeet() {
        val object1 = ObjectData(Pair(0.0, 0.0), Pair(1.0, 0.0))
        val object2 = ObjectData(Pair(5.0, 0.0), Pair(-1.0, 0.0))

        val meetingPoint = calculateMeetingPoint(object1, object2)

        assertEquals(Pair(2.5, 0.0), meetingPoint)
    }

    @Test
    fun testObjectsParallel() {
        val object1 = ObjectData(Pair(0.0, 0.0), Pair(1.0, 0.0))
        val object2 = ObjectData(Pair(0.0, 5.0), Pair(1.0, 0.0))

        val meetingPoint = calculateMeetingPoint(object1, object2)

        assertEquals(null, meetingPoint)
    }
}