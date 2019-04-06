package rmnvich.apps.coursework.modbus.data.utils

import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import java.lang.Math.abs
import java.nio.charset.Charset

class RegistersParser {

    fun parseIndications(response: ByteArray): String {
        val fractionalValue = formatTemperature(response[9], response[10], response[11], response[12])
        val temperature = response[7].toDouble() + abs(fractionalValue)
        return String.format("%.1f", temperature)
    }

    fun parseSensorIdentificationData(response: ByteArray): Sensor {
        val sensorData = arrayListOf<String>()
        val startIndex = 7

        val countOfStrings = response[startIndex]
        var index = startIndex + 1

        for (i in 0 until countOfStrings) {
            val stringLength = response[++index]
            val stringValue = response.copyOfRange(++index, index + stringLength)
                .toString(Charset.defaultCharset())

            sensorData.add(stringValue)
            index += stringLength
        }

        return Sensor(sensorData[0], sensorData[1], sensorData[2])
    }

    //TODO: Fix it
    private fun formatTemperature(loT: Byte, hiT: Byte, nTicksPerGrad: Byte, nTicksLeft: Byte): Double {
        if (hiT.toInt() != 0 && hiT.toInt() != 255) {
            return -255.0
        }
        if (loT.toInt() == 255 && hiT.toInt() == 255 && nTicksLeft.toInt() == 255)
            return -255.0

        var temp: Int = loT.toInt()
        if (hiT.toInt() != 0) {
            if (temp >= 128)
                temp = temp or 0xffffff00.toInt()
        } else {
            if (temp == 255) {
                temp = temp or 0xffffff00.toInt()
            }
        }
        val fTemp = temp / 2.0f
        temp /= 2

        var fRes = (temp.toDouble()) - 0.25 + (nTicksPerGrad.toInt() -
                nTicksLeft.toInt()).toDouble() / nTicksPerGrad.toDouble()

        if (abs(fRes - fTemp) > 0.5) {
            if (fRes < fTemp)
                fRes += 1
            else fRes -= 1
        }

        if (fRes > 100 || fRes < -100) {
            return -255.0
        }

        return fRes
    }
}