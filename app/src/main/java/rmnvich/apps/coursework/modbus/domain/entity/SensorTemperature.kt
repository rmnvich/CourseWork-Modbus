package rmnvich.apps.coursework.modbus.domain.entity

import rmnvich.apps.coursework.modbus.domain.entity.base.ISensor

class SensorTemperature : ISensor {

    override fun getRegisters(): ByteArray {
        return byteArrayOf(
            0x00, 0x03, 0x00, 0x00,
            0x00, 0x04, 0x45, 0xD8.toByte()
        )
    }
}