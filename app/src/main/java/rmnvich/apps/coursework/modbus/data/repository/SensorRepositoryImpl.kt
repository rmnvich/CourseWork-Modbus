package rmnvich.apps.coursework.modbus.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import com.ftdi.j2xx.D2xxManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import rmnvich.apps.coursework.modbus.data.utils.SensorFactory
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository

class SensorRepositoryImpl(
        private val applicationContext: Context,
        private val sensorFactory: SensorFactory,
        private val deviceManager: D2xxManager
) : SensorRepository {

    private val intentFilter = IntentFilter()

    init {
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
    }

    override fun searchSensor(): Flowable<Sensor> {
        return Flowable.create({ emitter ->
            getDeviceInfo(emitter)
            applicationContext.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    getDeviceInfo(emitter)
                }
            }, intentFilter)
        }, BackpressureStrategy.MISSING)
    }

    private fun getDeviceInfo(emitter: FlowableEmitter<Sensor>) {
        val devicesCount = deviceManager.createDeviceInfoList(applicationContext)
        if (devicesCount > 0) {
            val deviceList = arrayOfNulls<D2xxManager.FtDeviceInfoListNode>(devicesCount)
            deviceManager.getDeviceInfoList(devicesCount, deviceList)
            val device = deviceList[0]

            val sensorWithRegisters = sensorFactory.createSensorBySerialNumber(device?.serialNumber!!)
            //TODO: Get sensor readings

            val sensorWithInfo = Sensor(
                    getDeviceName(device.type),
                    device.serialNumber,
                    device.description,
                    device.id,
                    device.location
            )
            emitter.onNext(sensorWithInfo)
        } else emitter.onNext(Sensor("Device not found",
                "Device not found",
                "Device not found",
                0, 0))
    }

    private fun getDeviceName(deviceType: Int): String {
        return when (deviceType) {
            D2xxManager.FT_DEVICE_8U232AM -> "FT8U232AM device"
            D2xxManager.FT_DEVICE_X_SERIES -> "FTDI X_SERIES"
            D2xxManager.FT_DEVICE_UNKNOWN -> "Unknown device"
            D2xxManager.FT_DEVICE_2232H -> "FT2232H device"
            D2xxManager.FT_DEVICE_4232H -> "FT4232H device"
            D2xxManager.FT_DEVICE_232B -> "FT232B device"
            D2xxManager.FT_DEVICE_2232 -> "FT2232 device"
            D2xxManager.FT_DEVICE_232R -> "FT232R device"
            D2xxManager.FT_DEVICE_232H -> "FT232H device"
            else -> "FT232B device"
        }
    }

    private fun readSensor(sensor: Sensor): Flowable<Sensor> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeAddress(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}