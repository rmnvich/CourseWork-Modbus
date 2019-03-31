package rmnvich.apps.coursework.modbus.data.repository

import android.content.Context
import android.util.Log
import com.ftdi.j2xx.D2xxManager
import com.ftdi.j2xx.FT_Device
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import rmnvich.apps.coursework.modbus.data.utils.CRC16Calcer
import rmnvich.apps.coursework.modbus.data.utils.SensorFactory
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository
import java.nio.charset.Charset
import java.util.*

class SensorRepositoryImpl(
    private val applicationContext: Context,
    private val sensorFactory: SensorFactory,
    private val deviceManager: D2xxManager
) : SensorRepository {

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

                /**
                 * Ответ: 1 3 4 0 1 0 3 -21 -14
                 * 1 - Адрес устройства
                 * 3 - Код комманды
                 * 4 - Сколько байт в ответе
                 * 0 - Старший байт адреса (не нужен)
                 * 1 - Младший байт адреса (первый байт для всех запросов)
                 * 0 - Старший байт типа датчика (не нужен)
                 * 3 - Тип датчика
                 * Контрольная сумма
                 */

                /**
                 * Послать запрос на чтение первых двух регистров, чтобы получить первый байт для всех последующих запросов
                 * (чтобы знать адрес датчика с которым буду общаться)
                 *
                 * Далее послать запрос на идентификацию с этим первым байтом
                 *
                 * Преобразовать ответ в текст
                 *
                 * Из фабрики вытащить нужный датчик
                 *
                 * Сделать запрос с его регистрами
                 *
                 * Получить показания в байтах, преобразовать в текст
                 */

                //TODO: Запрос на чтение первых двух регистров
                val request = byteArrayOf(
                    0x00, 0x03, 0x00, 0x00,
                    0x00, 0x02, 0x00, 0x00
                )

                //TODO: Запрос идентификации
//                val request = byteArrayOf(
//                    0x01, 0x2B, 0x0E,
//                    0x01, 0x00, 0x00, 0x00
//                )


                // Расчёт контрольной суммы
                val crcCalcer = CRC16Calcer()
                val controlSum = crcCalcer.CalcCRC(request, 0, request.size - 2)

                request[request.size - 2] = controlSum.and(0xff).toByte()
                request[request.size - 1] = (controlSum / 256).and(0xff).toByte()

                device.purge(D2xxManager.FT_PURGE_RX)
                device.write(request)

                Thread.sleep(50)
                val countOfResponse = device.queueStatus

                val str = byteArrayOf(0x53, 0x63, 0x68)

                var sensorWithInfo: Sensor
                if (countOfResponse > 0) {
                    val count = device.read(response, countOfResponse, 5)
                    val string = StringBuilder("")
                    for (i in 0 until count)
                        string.append(response[i]).append(" ")

                    sensorWithInfo = Sensor(
                        "$controlSum",
                        "No data" + str.toString(Charset.defaultCharset()),
                        "No data" + 0x53.toChar() + 0x63.toChar() + 0x68.toChar(),
                        "$string"
                    )
                } else {
                    sensorWithInfo = Sensor(
                        "$controlSum",
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