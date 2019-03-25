package rmnvich.apps.coursework.modbus.presentation.fragment.sensorwrite

import rmnvich.apps.coursework.modbus.presentation.mvp.MvpPresenter
import rmnvich.apps.coursework.modbus.presentation.mvp.MvpView

interface SensorWriteAddressContract {

    interface View : MvpView

    interface Presenter : MvpPresenter<View>
}