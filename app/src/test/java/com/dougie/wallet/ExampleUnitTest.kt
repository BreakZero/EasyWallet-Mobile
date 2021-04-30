package com.dougie.wallet

import kotlinx.coroutines.flow.*
import org.junit.Assert.*
import org.junit.Test
import org.web3j.utils.Numeric

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val hexByte = Numeric.cleanHexPrefix("0x5208")

        val result = MutableStateFlow<Int>(1)
            .debounce(12)
            .distinctUntilChanged()
        println(result)

        println(hexByte)
    }
}
