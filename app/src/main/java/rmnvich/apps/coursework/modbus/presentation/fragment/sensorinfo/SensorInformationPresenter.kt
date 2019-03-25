package rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo

import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.coursework.modbus.domain.interactor.sensorinfo.SensorInformationInteractor
import rmnvich.apps.coursework.modbus.presentation.mvp.PresenterBase

class SensorInformationPresenter(
        private val compositeDisposable: CompositeDisposable,
        private val interactor: SensorInformationInteractor
) : PresenterBase<SensorInformationContract.View>(),
        SensorInformationContract.Presenter {

    override fun viewIsReady() {
        view?.showProgress()
        compositeDisposable.add(
                interactor.searchSensor()
                        .subscribe({
                            view?.hideProgress()
                            view?.displaySensorInfo(it)
                        }, {
                            view?.hideProgress()
                            view?.showErrorMessage(it.message!!)
                        })
        )
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }
}