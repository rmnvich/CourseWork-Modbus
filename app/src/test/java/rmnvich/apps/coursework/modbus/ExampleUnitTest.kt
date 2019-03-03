package rmnvich.apps.coursework.modbus

import org.junit.Test

class ExampleUnitTest {

    @Test
    fun convertIntToByteWithBinaryInvert() {
        println(0xD8.toByte())
    }

    @Test
    fun printByteArray() {
        val byteArray: ByteArray = byteArrayOf(
            0x00, 0x03, 0x00, 0x00,
            0x00, 0x04, 0x45, 0xD8.toByte()
        )

        for (i in 0 until byteArray.size)
            println(byteArray[i])
    }
}