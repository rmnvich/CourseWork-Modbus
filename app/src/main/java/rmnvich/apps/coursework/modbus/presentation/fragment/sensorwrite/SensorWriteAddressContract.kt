package rmnvich.apps.coursework.modbus.presentation.fragment.sensorwrite

import rmnvich.apps.coursework.modbus.presentation.mvp.MvpPresenter
import rmnvich.apps.coursework.modbus.presentation.mvp.MvpView

interface SensorWriteAddressContract {

    interface View : MvpView {

        fun showErrorMessage(message: String)
    }

    interface Presenter : MvpPresenter<View>
}