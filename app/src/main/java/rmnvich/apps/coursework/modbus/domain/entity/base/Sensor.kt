package rmnvich.apps.coursework.modbus.domain.entity.base

open class Sensor(
    var sensorName: String,
    var sensorSerialNumber: String,
    var sensorDescription: String,
    var sensorId: Int,
    var sensorLocation: Int
)