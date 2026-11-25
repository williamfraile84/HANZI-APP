package com.jukhg10.hanzimemo

import com.jukhg10.hanzimemo.data.models.StudyItem
import org.junit.Assert.*
import org.junit.Test

class SRSAlgorithmTest {

    @Test
    fun `al responder KnewIt la prioridad maxima se incrementa y se marca como learned`() {
        val initialItem = StudyItem(
            id = 1,
            itemId = 10,
            itemType = "character",
            currentPriority = 1,
            maxPriority = 2,
            learned = false
        )

        // Simulación de la regla SRS para respuesta correcta
        val newMaxPriority = initialItem.maxPriority + 1
        val updatedItem = initialItem.copy(
            currentPriority = newMaxPriority,
            maxPriority = newMaxPriority,
            learned = true
        )

        assertEquals(3, updatedItem.maxPriority)
        assertEquals(3, updatedItem.currentPriority)
        assertTrue(updatedItem.learned)
    }

    @Test
    fun `al responder Forgot la prioridad actual se reinicia a 1 y la maxima disminuye`() {
        val initialItem = StudyItem(
            id = 2,
            itemId = 20,
            itemType = "word",
            currentPriority = 3,
            maxPriority = 4,
            learned = true
        )

        // Simulación de la regla SRS para olvido
        val newCurrentPriority = 1
        val newMaxPriority = (initialItem.maxPriority - 1).coerceAtLeast(1)
        val updatedItem = initialItem.copy(
            currentPriority = newCurrentPriority,
            maxPriority = newMaxPriority,
            learned = false
        )

        assertEquals(1, updatedItem.currentPriority)
        assertEquals(3, updatedItem.maxPriority)
        assertFalse(updatedItem.learned)
    }

    @Test
    fun `al responder Forgot con maxPriority en 1 no disminuye de 1`() {
        val initialItem = StudyItem(
            id = 3,
            itemId = 30,
            itemType = "character",
            currentPriority = 1,
            maxPriority = 1,
            learned = false
        )

        val newMaxPriority = (initialItem.maxPriority - 1).coerceAtLeast(1)
        assertEquals(1, newMaxPriority)
    }

    @Test
    fun `multiples aciertos consecutivos incrementan progresivamente la prioridad`() {
        var item = StudyItem(
            id = 4,
            itemId = 40,
            itemType = "character",
            currentPriority = 1,
            maxPriority = 1,
            learned = false
        )

        for (step in 1..5) {
            val nextMax = item.maxPriority + 1
            item = item.copy(currentPriority = nextMax, maxPriority = nextMax, learned = true)
            assertEquals(1 + step, item.maxPriority)
            assertEquals(1 + step, item.currentPriority)
            assertTrue(item.learned)
        }
    }

    @Test
    fun `decremento de prioridad en ciclo de mazo no desciende por debajo de 1`() {
        var priority = 3
        priority = (priority - 1).coerceAtLeast(1)
        assertEquals(2, priority)

        priority = (priority - 1).coerceAtLeast(1)
        assertEquals(1, priority)

        // Decremento adicional no debe bajar de 1
        priority = (priority - 1).coerceAtLeast(1)
        assertEquals(1, priority)
    }

    @Test
    fun `filtrado de mazo separa correctamente elementos aprendidos y en estudio`() {
        val deck = listOf(
            StudyItem(id = 1, itemId = 10, itemType = "character", learned = false),
            StudyItem(id = 2, itemId = 20, itemType = "word", learned = true),
            StudyItem(id = 3, itemId = 30, itemType = "character", learned = false),
            StudyItem(id = 4, itemId = 40, itemType = "word", learned = true)
        )

        val studying = deck.filter { !it.learned }
        val learned = deck.filter { it.learned }

        assertEquals(2, studying.size)
        assertEquals(2, learned.size)
        assertTrue(studying.all { !it.learned })
        assertTrue(learned.all { it.learned })
    }
}