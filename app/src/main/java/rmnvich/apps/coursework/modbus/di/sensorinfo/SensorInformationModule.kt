package rmnvich.apps.coursework.modbus.di.sensorinfo

import com.rmnvich.roomtest.app.dagger.PerFragment
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.coursework.modbus.domain.interactor.sensorinfo.SensorInformationInteractor
import rmnvich.apps.coursework.modbus.domain.repository.SensorRepository
import rmnvich.apps.coursework.modbus.domain.utils.SchedulersProvider
import rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo.SensorInformationPresenter

@Module
class SensorInformationModule {

    @PerFragment
    @Provides
    fun providePresenter(
        compositeDisposable: CompositeDisposable,
        interactor: SensorInformationInteractor
    ): SensorInformationPresenter {
        return SensorInformationPresenter(compositeDisposable, interactor)
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
    ): SensorInformationInteractor {
        return SensorInformationInteractor(schedulersProvider, sensorRepository)
    }

    @PerFragment
    @Provides
    fun provideSchedulersProvider(): SchedulersProvider {
        return SchedulersProvider()
    }
}