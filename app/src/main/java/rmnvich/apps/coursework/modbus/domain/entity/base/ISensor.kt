package rmnvich.apps.coursework.modbus.domain.entity.base

interface ISensor {

    fun getRegisters(): ByteArray
}