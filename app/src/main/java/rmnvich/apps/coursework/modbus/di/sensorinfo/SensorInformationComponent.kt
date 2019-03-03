package rmnvich.apps.coursework.modbus.di.sensorinfo

import com.rmnvich.roomtest.app.dagger.PerFragment
import dagger.Subcomponent
import rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo.SensorInformationFragment

@PerFragment
@Subcomponent(modules = [(SensorInformationModule::class)])
interface SensorInformationComponent {
    fun inject(fragment: SensorInformationFragment)
}