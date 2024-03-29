package rmnvich.apps.coursework.modbus.domain.interactor.sensorinfo

import io.reactivex.Flowable
import rmnvich.apps.coursework.modbus.data.common.Constants.DEFAULT_DELAY
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository
import rmnvich.apps.coursework.modbus.domain.utils.SchedulersProvider
import java.util.concurrent.TimeUnit

class SensorInformationInteractor(
        private val schedulersProvider: SchedulersProvider,
        private val sensorRepository: SensorRepository
) {

    fun readSensorData(): Flowable<Sensor> {
        return sensorRepository.readSensorData()
                .subscribeOn(schedulersProvider.io())
                .delay(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }
}