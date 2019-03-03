package rmnvich.apps.coursework.modbus.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor

interface SensorRepository {

    fun searchSensor(): Flowable<Sensor>

    fun writeAddress(): Completable
}