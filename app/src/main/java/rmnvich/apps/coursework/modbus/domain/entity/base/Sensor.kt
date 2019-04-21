package rmnvich.apps.coursework.modbus.domain.entity.base

open class Sensor(
        var sensorName: String,
        var sensorManufacturer: String,
        var sensorVersion: String
) {

    var sensorSerialNumber: String? = null
    var sensorNetworkAddress: String? = null
    var sensorIndications: Any = Any()
}