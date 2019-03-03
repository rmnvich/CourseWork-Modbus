package rmnvich.apps.coursework.modbus.di.sensorwrite

import com.rmnvich.roomtest.app.dagger.PerFragment
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.coursework.modbus.domain.interactor.sensorwrite.SensorWriteAddressInteractor
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository
import rmnvich.apps.coursework.modbus.domain.utils.SchedulersProvider
import rmnvich.apps.coursework.modbus.presentation.fragment.sensorwrite.SensorWriteAddressPresenter

@Module
class SensorWriteAddressModule {

    @PerFragment
    @Provides
    fun providePresenter(
        compositeDisposable: CompositeDisposable,
        interactor: SensorWriteAddressInteractor
    ): SensorWriteAddressPresenter {
        return SensorWriteAddressPresenter(compositeDisposable, interactor)
    }

    @PerFragment
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @PerFragment
    @Provides
    fun provideInteractor(
        schedulersProvider: SchedulersProvider,
        sensorRepository: SensorRepository
    ): SensorWriteAddressInteractor {
        return SensorWriteAddressInteractor(schedulersProvider, sensorRepository)
    }

    @PerFragment
    @Provides
    fun provideSchedulersProvider(): SchedulersProvider {
        return SchedulersProvider()
    }
}