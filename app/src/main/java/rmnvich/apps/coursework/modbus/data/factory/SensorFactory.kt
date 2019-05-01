package rmnvich.apps.coursework.modbus.data.factory

import rmnvich.apps.coursework.modbus.data.common.Constants.SENSOR_PRESSURE_NAME
import rmnvich.apps.coursework.modbus.data.common.Constants.SENSOR_TEMPERATURE_NAME
import rmnvich.apps.coursework.modbus.domain.entity.SensorPressure
import rmnvich.apps.coursework.modbus.domain.entity.SensorTemperature
import rmnvich.apps.coursework.modbus.domain.entity.base.ISensor

class SensorFactory {

    fun createSensorBySensorName(deviceName: String): ISensor {
        return when (deviceName) {
            SENSOR_TEMPERATURE_NAME -> SensorTemperature()
            SENSOR_PRESSURE_NAME -> SensorPressure()
            else -> SensorTemperature()
        }
    }
}