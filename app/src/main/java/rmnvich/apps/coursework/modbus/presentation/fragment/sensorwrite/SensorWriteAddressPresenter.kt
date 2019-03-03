package rmnvich.apps.coursework.modbus.presentation.fragment.sensorwrite

import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.coursework.modbus.domain.interactor.sensorwrite.SensorWriteAddressInteractor
import rmnvich.apps.coursework.modbus.presentation.mvp.PresenterBase

class SensorWriteAddressPresenter(
    private val compositeDisposable: CompositeDisposable,
    private val interactor: SensorWriteAddressInteractor
) : PresenterBase<SensorWriteAddressContract.View>(), SensorWriteAddressContract.Presenter {

    override fun viewIsReady() {

    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }
}