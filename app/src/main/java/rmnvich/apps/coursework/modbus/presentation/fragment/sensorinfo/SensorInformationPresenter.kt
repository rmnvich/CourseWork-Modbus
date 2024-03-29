package rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.USB_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.coursework.modbus.domain.interactor.sensorinfo.SensorInformationInteractor
import rmnvich.apps.coursework.modbus.presentation.mvp.PresenterBase
import timber.log.Timber


class SensorInformationPresenter(
    private val interactor: SensorInformationInteractor,
    private val compositeDisposable: CompositeDisposable
) : PresenterBase<SensorInformationContract.View>(),
    SensorInformationContract.Presenter {

    override fun viewIsReady() {
        registerBroadcastReceiver()

        val isDeviceConnected = checkIfDeviceConnected()
        if (isDeviceConnected) {
            view?.setDeviceConnectionStatus(true)
            view?.showProgress()

            startReadingSensor()
        } else view?.setDeviceConnectionStatus(false)
    }

    private fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)

        (view as SensorInformationFragment).activity
            ?.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent?.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
                        startReadingSensor()
                        view?.showProgress()
                        view?.setDeviceConnectionStatus(true)
                    } else if (intent?.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
                        view?.setDeviceConnectionStatus(false)
                    }
                }
            }, intentFilter)
    }

    override fun checkIfDeviceConnected(): Boolean {
        val usbManager = (view as SensorInformationFragment).activity
            ?.application?.getSystemService(USB_SERVICE) as UsbManager
        return usbManager.deviceList.isNotEmpty()
    }

    override fun startReadingSensor() {
        compositeDisposable.add(
            interactor.readSensorData()
                .subscribe({
                    view?.displaySensorInfo(it)
                    view?.hideProgress()
                }, { view?.hideProgress() },
                    { view?.hideProgress() })
        )
    }
}