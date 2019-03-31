package rmnvich.apps.coursework.modbus.data.utils

import rmnvich.apps.coursework.modbus.data.common.Constants.SENSOR_TEMPERATURE_NAME
import rmnvich.apps.coursework.modbus.domain.entity.SensorTemperature
import rmnvich.apps.coursework.modbus.domain.entity.base.ISensor

class SensorFactory {

    fun createSensorBySensorName(deviceName: String): ISensor {
        return when (deviceName) {
            SENSOR_TEMPERATURE_NAME -> SensorTemperature()
            else -> SensorTemperature()
        }
    }
}