package rmnvich.apps.coursework.modbus.domain.entity

import rmnvich.apps.coursework.modbus.domain.entity.base.ISensor

class SensorTemperature : ISensor {

    override fun getRegistersForIndications(deviceAddressByte: Byte): ByteArray {
        return byteArrayOf(
            deviceAddressByte, 0x03, 0x00, 0x00,
            0x00, 0x04, 0x45, 0xD8.toByte()
        )
    }

    override fun getRegistersForSerialNumber(deviceAddressByte: Byte): ByteArray {
        return byteArrayOf(
            deviceAddressByte, 0x03, 0x00, 0x07,
            0x00, 0x02, 0x45, 0xD8.toByte()
        )
    }

}