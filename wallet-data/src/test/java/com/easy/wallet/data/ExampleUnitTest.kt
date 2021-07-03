package com.easy.wallet.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val value = "115792089237316195423570985008687907853269984665640564039457584007913129639935".toBigDecimal()
        println(value.toString())
        assertEquals(4, 2 + 2)
    }
}
