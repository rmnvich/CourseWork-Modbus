package rmnvich.apps.coursework.modbus.di.app

import android.content.Context
import com.ftdi.j2xx.D2xxManager
import com.rmnvich.roomtest.app.dagger.PerApplication
import dagger.Module
import dagger.Provides
import rmnvich.apps.coursework.modbus.data.repository.SensorRepositoryImpl
import rmnvich.apps.coursework.modbus.data.utils.SensorFactory
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository

@Module
class AppModule(private val applicationContext: Context) {

    @PerApplication
    @Provides
    fun provideSensorRepository(
        sensorFactory: SensorFactory,
        deviceManager: D2xxManager
    ): SensorRepository {
        return SensorRepositoryImpl(applicationContext, sensorFactory, deviceManager)
    }

    @PerApplication
    @Provides
    fun provideSensorFactory(): SensorFactory {
        return SensorFactory()
    }

    @PerApplication
    @Provides
    fun provideDeviceManager(): D2xxManager {
        return D2xxManager.getInstance(applicationContext)
    }
}