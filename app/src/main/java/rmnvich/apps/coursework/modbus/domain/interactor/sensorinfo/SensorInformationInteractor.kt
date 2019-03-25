package rmnvich.apps.coursework.modbus.domain.interactor.sensorinfo

import io.reactivex.Flowable
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository
import rmnvich.apps.coursework.modbus.domain.utils.SchedulersProvider
import java.util.concurrent.TimeUnit

class SensorInformationInteractor(
        private val schedulersProvider: SchedulersProvider,
        private val sensorRepository: SensorRepository
) {

    fun searchSensor(): Flowable<Sensor> {
        return sensorRepository.searchSensor()
                .subscribeOn(schedulersProvider.io())
                .delay(1000, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }
}