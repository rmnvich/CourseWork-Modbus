package rmnvich.apps.coursework.modbus.domain.entity.base

interface ISensor {

    fun getRegistersForIndications(deviceAddressByte: Byte): ByteArray

    fun getRegistersForSerialNumber(deviceAddressByte: Byte): ByteArray
}