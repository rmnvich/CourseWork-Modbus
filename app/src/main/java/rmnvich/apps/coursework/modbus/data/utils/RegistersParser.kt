package rmnvich.apps.coursework.modbus.data.utils

import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import java.nio.charset.Charset

class RegistersParser {

    fun parseTemperature(byteArray: ByteArray): String {

        return ""
    }

    // TODO: fix it!
    fun parseSensorIdentificationData(response: ByteArray): Sensor {
        val sensor = Sensor()

        val sensorManufacturer = response.copyOfRange(10, 16).toString(Charset.defaultCharset())
        val sensorName = response.copyOfRange(18, 24).toString(Charset.defaultCharset())
        val sensorVersion = response.copyOfRange(26, 30).toString(Charset.defaultCharset())

        sensor.sensorManufacturer = sensorManufacturer
        sensor.sensorName = sensorName
        sensor.sensorVersion = sensorVersion

        return sensor
    }

    /*
    private fun formatTemperature(loT: Byte, hiT: Byte, nTicksPerGrad: Byte, nTicksLeft: Byte): Double {
        if (hiT.toInt() != 0 && hiT.toInt() != 255) {
            return -255.0
        }
        if (loT.toInt() == 255 && hiT.toInt() == 255 && nTicksLeft.toInt() == 255)
            return -255.0

        val temp: Int = loT.toInt()
        if (hiT.toInt() != 0) {
            if (temp >= 128)
                temp |= 0xffffff00
        } else {
            if (temp == 255) {
                temp |= 0xffffff00
            }
        }
        val fTemp: Float = temp / 2.0f
        temp >>= 1

        val fRes = (temp.toDouble()) - 0.25 + (nTicksPerGrad.toInt() - nTicksLeft.toInt()).toDouble() / nTicksPerGrad.toDouble()
        if (fabs(fRes - fTemp) > 0.5) {
            if (fRes < fTemp)
                fRes += 1
            else fRes -= 1
        }
        if (fRes > 100 || fRes < -100) {
            return -255.0
        }
        return fRes
    }
    */
}