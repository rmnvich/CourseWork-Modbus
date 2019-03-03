package rmnvich.apps.coursework.modbus.di

import android.content.Context
import rmnvich.apps.coursework.modbus.di.app.AppComponent
import rmnvich.apps.coursework.modbus.di.app.AppModule
import rmnvich.apps.coursework.modbus.di.app.DaggerAppComponent
import rmnvich.apps.coursework.modbus.di.sensorinfo.SensorInformationComponent
import rmnvich.apps.coursework.modbus.di.sensorinfo.SensorInformationModule
import rmnvich.apps.coursework.modbus.di.sensorwrite.SensorWriteAddressComponent
import rmnvich.apps.coursework.modbus.di.sensorwrite.SensorWriteAddressModule

class ComponentHolder(var context: Context) {

    private var mAppComponent: AppComponent? = null
    private var mSensorInformationComponent: SensorInformationComponent? = null
    private var mSensorWriteAddressComponent: SensorWriteAddressComponent? = null

    fun init() {
        mAppComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context)).build()
    }

    fun getSensorInformationComponent(): SensorInformationComponent? {
        if (mSensorInformationComponent == null) {
            mSensorInformationComponent = mAppComponent?.createSensorInformationComponent(
                SensorInformationModule()
            )
        }
        return mSensorInformationComponent
    }

    fun getSensorWriteAddressComponent(): SensorWriteAddressComponent? {
        if (mSensorWriteAddressComponent == null) {
            mSensorWriteAddressComponent = mAppComponent?.createSensorWriteAddressComponent(
                SensorWriteAddressModule()
            )
        }
        return mSensorWriteAddressComponent
    }

    fun releaseSensorInformationComponent() {
        mSensorInformationComponent = null
    }

    fun releaseSensorWriteAddressComponent() {
        mSensorWriteAddressComponent = null
    }
}