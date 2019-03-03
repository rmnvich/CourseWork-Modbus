package rmnvich.apps.coursework.modbus.di.sensorwrite

import com.rmnvich.roomtest.app.dagger.PerFragment
import dagger.Subcomponent
import rmnvich.apps.coursework.modbus.presentation.fragment.sensorwrite.SensorWriteAddressFragment

@PerFragment
@Subcomponent(modules = [(SensorWriteAddressModule::class)])
interface SensorWriteAddressComponent {
    fun inject(fragment: SensorWriteAddressFragment)
}