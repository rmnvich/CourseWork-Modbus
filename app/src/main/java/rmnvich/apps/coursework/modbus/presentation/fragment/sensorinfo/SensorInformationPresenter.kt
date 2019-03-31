package rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.coursework.modbus.domain.interactor.sensorinfo.SensorInformationInteractor
import rmnvich.apps.coursework.modbus.presentation.mvp.PresenterBase
import android.content.Context.USB_SERVICE
import android.util.Log
import timber.log.Timber


class SensorInformationPresenter(
        private val interactor: SensorInformationInteractor,
        private val compositeDisposable: CompositeDisposable
) : PresenterBase<SensorInformationContract.View>(),
        SensorInformationContract.Presenter {

    private var isPlaceholderHidden = false

    override fun viewIsReady() {
        registerBroadcastRecevier()

        val isDeviceConnected = checkIfDeviceConnected()
        if (isDeviceConnected) {
            view?.setDeviceAttachingStatus(true, isDefaultValue = true)
        } else view?.setDeviceAttachingStatus(false, isDefaultValue = true)

        startReadingSensor()
    }

    private fun registerBroadcastRecevier() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)

        (view as SensorInformationFragment).activity
                ?.registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        if (intent?.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
                            startReadingSensor()
                            view?.setDeviceAttachingStatus(true, isDefaultValue = false)
                        } else if (intent?.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
                            view?.setDeviceAttachingStatus(false, isDefaultValue = false)
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
        compositeDisposable.add(interactor.searchSensor()
                .subscribe({
                    view?.displaySensorInfo(it)

                    if (!isPlaceholderHidden) {
                        view?.hidePlaceholders()
                        isPlaceholderHidden = true
                    }
                }, { Timber.e(it) })
        )
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }
}