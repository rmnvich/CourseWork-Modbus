package rmnvich.apps.coursework.modbus.domain.interactor.sensorwrite

import io.reactivex.Completable
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository
import rmnvich.apps.coursework.modbus.domain.utils.SchedulersProvider

class SensorWriteAddressInteractor(
    private val schedulersProvider: SchedulersProvider,
    private val sensorRepository: SensorRepository
) {

    fun writeAddress(): Completable {
        return sensorRepository.writeAddress()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }
}