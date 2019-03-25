package rmnvich.apps.coursework.modbus.data.repository

import android.content.Context
import com.ftdi.j2xx.D2xxManager
import com.ftdi.j2xx.FT_Device
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import rmnvich.apps.coursework.modbus.data.utils.SensorFactory
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository
import java.util.*

class SensorRepositoryImpl(
        private val applicationContext: Context,
        private val sensorFactory: SensorFactory,
        private val deviceManager: D2xxManager
) : SensorRepository {

    //TODO: комманда идентификации: 0x00, 0x43, 0x14, 0x01, 0x00

    //TODO: получаем имя датчика, фирму и версию, и исходя из фабрики шлём конкретные регистры

    //TODO: выводить имя датчика, фирму, версию и показания

    override fun searchSensor(): Flowable<Sensor> {
        var device: FT_Device
        return Flowable.create({ emitter ->
            val devicesCount = deviceManager.createDeviceInfoList(applicationContext)
            val deviceList = arrayOfNulls<D2xxManager.FtDeviceInfoListNode>(devicesCount)
            deviceManager.getDeviceInfoList(devicesCount, deviceList)

            device = deviceManager.openBySerialNumber(
                    applicationContext,
                    deviceList[0]?.serialNumber
            )
            device.setBitMode(0.toByte(), D2xxManager.FT_BITMODE_RESET)
            device.setBaudRate(9600)
            device.setDataCharacteristics(
                    D2xxManager.FT_DATA_BITS_8,
                    D2xxManager.FT_STOP_BITS_1,
                    D2xxManager.FT_PARITY_NONE
            )
            device.latencyTimer = 5.toByte()
            device.setRts()
            device.clrDtr()
            device.setFlowControl(D2xxManager.FT_FLOW_NONE, 0x11, 0x13)

            val calendar = Calendar.getInstance()
            val response = ByteArray(256)
            while (true) {
                val currentTime = calendar.timeInMillis

                val request = byteArrayOf(
                        0x00, 0x03, 0x00, 0x00,
                        0x00, 0x04, 0x45, 0xD8.toByte()
                )

                // Identification (not working)
//                val request = byteArrayOf(
//                    0x00, 0x2B, 0x0E,
//                    0x01, 0x00
//                )

                device.purge(D2xxManager.FT_PURGE_RX)
                device.write(request)

                Thread.sleep(50)
                val countOfResponse = device.queueStatus

                var sensorWithInfo: Sensor
                if (countOfResponse > 0) {
                    val count = device.read(response, countOfResponse, 5)
                    val string = StringBuilder("")
                    for (i in 0 until count)
                        string.append(response[i]).append(" ")

                    sensorWithInfo = Sensor(
                            "No data",
                            "No data",
                            "No data",
                            "$string"
                    )
                } else {
                    sensorWithInfo = Sensor(
                            "No data",
                            "No data",
                            "No data",
                            "Timeout"
                    )
                }
                emitter.onNext(sensorWithInfo)

                val sleepTime = 500 - (calendar.timeInMillis - currentTime)
                if (sleepTime > 0)
                    Thread.sleep(sleepTime)
            }
        }, BackpressureStrategy.DROP)
    }

    private fun readSensor(sensor: Sensor): Flowable<Sensor> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeAddress(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}