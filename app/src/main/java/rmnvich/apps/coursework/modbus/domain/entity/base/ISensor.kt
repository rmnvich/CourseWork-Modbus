package rmnvich.apps.coursework.modbus.domain.entity.base

interface ISensor {

    fun getRegisters(deviceAddressByte: Byte): ByteArray
}