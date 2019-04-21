package rmnvich.apps.coursework.modbus.data.repository

import android.content.Context
import com.ftdi.j2xx.D2xxManager
import com.ftdi.j2xx.FT_Device
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import rmnvich.apps.coursework.modbus.R
import rmnvich.apps.coursework.modbus.data.utils.Constants.SENSOR_INDICATIONS_ERROR
import rmnvich.apps.coursework.modbus.data.utils.ControlSumCalculator
import rmnvich.apps.coursework.modbus.data.utils.RegistersParser
import rmnvich.apps.coursework.modbus.data.utils.SensorFactory
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository
import java.util.*

class SensorRepositoryImpl(
        private val applicationContext: Context,
        private val sensorFactory: SensorFactory,
        private val deviceManager: D2xxManager,
        private val controlSumCalculator: ControlSumCalculator,
        private val registersParser: RegistersParser
) : SensorRepository {

    private lateinit var device: FT_Device

    private val searchDeviceRequest = byteArrayOf(
            0x00, 0x03, 0x00, 0x00,
            0x00, 0x02, 0x00, 0x00
    )

    private var identificationRequest = byteArrayOf(
            0x00, 0x2B, 0x0E, 0x01,
            0x00, 0x00, 0x00
    )

    // serialNumber (6 (версия прошивки), 7, 8 register in identification)

    override fun readSensorData(): Flowable<Sensor> {
        return Flowable.create({ emitter ->
            searchDevice()

            /**
             * Послать запрос идентификации ещё раз, только вместо первого региста 0x00 слать 0x07 и 0x08 ?
             * Сделать обратно первый регистр 0x00
             */

            // Getting first byte for next requests
            val deviceAddressByte = sendRequest(searchDeviceRequest)[4]
            identificationRequest[0] = deviceAddressByte

            // Identification sensor and parse response
            val identificationResponse = sendRequest(identificationRequest)
            val sensor = registersParser.parseSensorIdentificationData(identificationResponse)

            // Set sensorNetworkAddress
            sensor.sensorNetworkAddress = deviceAddressByte.toString()

            // Create necessary sensor by sensor name
            val iSensorWithRegisters = sensorFactory.createSensorBySensorName(sensor.sensorName)
            val indicationsRequest = iSensorWithRegisters.getRegisters(deviceAddressByte)

            // Getting sensor indications, parsing and emitting their
            while (true) {
                val indicationsResponse = sendRequest(indicationsRequest)
                var parsedIndications = registersParser.parseIndications(indicationsResponse)

                if (parsedIndications == SENSOR_INDICATIONS_ERROR)
                    parsedIndications = applicationContext.getString(R.string.sensor_indications_error)

                sensor.sensorIndications = parsedIndications
                emitter.onNext(sensor)
            }
        }, BackpressureStrategy.DROP)
    }

    private fun sendRequest(request: ByteArray): ByteArray {
        val calendar = Calendar.getInstance()
        val response = ByteArray(256)
        while (true) {
            val currentTime = calendar.timeInMillis

            // Calculating control sum
            val controlSum = controlSumCalculator.calculateControlSum(
                    request, 0,
                    request.size - 2
            )

            // Adding control sum to two latest bytes of request
            request[request.size - 2] = controlSum.and(0xff).toByte()
            request[request.size - 1] = (controlSum / 256).and(0xff).toByte()

            // Send request
            device.purge(D2xxManager.FT_PURGE_RX)
            device.write(request)

            Thread.sleep(150)
            val countOfResponse = device.queueStatus

            if (countOfResponse > 0) {
                // Read response
                device.read(response, countOfResponse, 5)
                return response.copyOfRange(0, countOfResponse)
            }

            // Buffer overflow protection
            val sleepTime = 500 - (calendar.timeInMillis - currentTime)
            if (sleepTime > 0)
                Thread.sleep(sleepTime)
        }
    }

    /**
     * The function pings the connection by usb until it finds the device and sets it up
     * @return is the device found
     */
    private fun searchDevice() {
        while (true) {
            val devicesCount = deviceManager.createDeviceInfoList(applicationContext)
            val deviceList = arrayOfNulls<D2xxManager.FtDeviceInfoListNode>(devicesCount)
            deviceManager.getDeviceInfoList(devicesCount, deviceList)

            if (devicesCount > 0) {
                device = deviceManager.openBySerialNumber(
                        applicationContext,
                        deviceList[0]?.serialNumber
                )
                settingDevice()

                return
            }
        }
    }

    private fun settingDevice() {
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
    }

    override fun writeAddress(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}