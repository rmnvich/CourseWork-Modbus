package rmnvich.apps.coursework.modbus.data.utils

import rmnvich.apps.coursework.modbus.data.utils.Constants.SENSOR_INDICATIONS_ERROR
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import java.nio.charset.Charset
import kotlin.math.abs

class RegistersParser {

    fun parseIndications(response: ByteArray): String {
        val errorCode = response[5]

        return if (errorCode.toInt() == 0) {
            val beforeDot = response[7].toDouble()
            var afterDot = response[8].toDouble() / 256.0

            if (afterDot < 0)
                afterDot = 1 - abs(afterDot)

            String.format("%.1f", beforeDot + afterDot)
        } else SENSOR_INDICATIONS_ERROR
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

    fun parseSensorSerialNumber(response: ByteArray): String {
        return ((response[4] * 256 * 256) + (response[5] * 256) + (response[6])).toString()
    }
}