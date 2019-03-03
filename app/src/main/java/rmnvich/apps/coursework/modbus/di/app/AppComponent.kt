package rmnvich.apps.coursework.modbus.di.app

import com.rmnvich.roomtest.app.dagger.PerApplication
import dagger.Component
import rmnvich.apps.coursework.modbus.di.sensorinfo.SensorInformationComponent
import rmnvich.apps.coursework.modbus.di.sensorinfo.SensorInformationModule
import rmnvich.apps.coursework.modbus.di.sensorwrite.SensorWriteAddressComponent
import rmnvich.apps.coursework.modbus.di.sensorwrite.SensorWriteAddressModule

@PerApplication
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun createSensorInformationComponent(module: SensorInformationModule): SensorInformationComponent

    fun createSensorWriteAddressComponent(module: SensorWriteAddressModule): SensorWriteAddressComponent
}