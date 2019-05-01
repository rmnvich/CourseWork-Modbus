package rmnvich.apps.coursework.modbus.di.app

import android.content.Context
import com.ftdi.j2xx.D2xxManager
import com.rmnvich.roomtest.app.dagger.PerApplication
import dagger.Module
import dagger.Provides
import rmnvich.apps.coursework.modbus.data.repository.SensorRepositoryImpl
import rmnvich.apps.coursework.modbus.data.utils.ControlSumCalculator
import rmnvich.apps.coursework.modbus.data.utils.RegistersParser
import rmnvich.apps.coursework.modbus.data.factory.SensorFactory
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository

@Module
class AppModule(private val applicationContext: Context) {

    @PerApplication
    @Provides
    fun provideSensorRepository(
        sensorFactory: SensorFactory,
        deviceManager: D2xxManager,
        controlSumCalculator: ControlSumCalculator,
        registersParser: RegistersParser
    ): SensorRepository {
        return SensorRepositoryImpl(
            applicationContext, sensorFactory,
            deviceManager, controlSumCalculator,
            registersParser
        )
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

    @PerApplication
    @Provides
    fun provideControlSumCalculator(): ControlSumCalculator {
        return ControlSumCalculator()
    }

    @PerApplication
    @Provides
    fun provideRegistersParser(): RegistersParser {
        return RegistersParser()
    }
}