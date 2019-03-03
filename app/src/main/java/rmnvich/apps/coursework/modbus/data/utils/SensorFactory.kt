package rmnvich.apps.coursework.modbus.data.utils

import rmnvich.apps.coursework.modbus.data.common.Constants.SERIAL_SENSOR_TEMPERATURE
import rmnvich.apps.coursework.modbus.domain.entity.SensorTemperature
import rmnvich.apps.coursework.modbus.domain.entity.base.ISensor

class SensorFactory {

    fun createSensorBySerialNumber(serialNumber: String): ISensor {
        return when (serialNumber) {
            SERIAL_SENSOR_TEMPERATURE -> SensorTemperature()
            else -> SensorTemperature()
        }
    }
}