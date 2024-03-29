package rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo

import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import rmnvich.apps.coursework.modbus.presentation.mvp.MvpPresenter
import rmnvich.apps.coursework.modbus.presentation.mvp.MvpView

interface SensorInformationContract {

    interface View : MvpView {

        fun displaySensorInfo(sensor: Sensor)

        fun setTextViewsInformationColor(colors: Array<Int>)

        fun setDeviceConnectionStatus(isConnected: Boolean)

        fun showProgress()

        fun hideProgress()
    }

    interface Presenter : MvpPresenter<View> {

        fun checkIfDeviceConnected(): Boolean

        fun startReadingSensor()
    }
}