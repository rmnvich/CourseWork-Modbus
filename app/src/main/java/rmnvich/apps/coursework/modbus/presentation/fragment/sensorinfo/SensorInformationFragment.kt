package rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rmnvich.apps.coursework.modbus.App
import rmnvich.apps.coursework.modbus.R
import rmnvich.apps.coursework.modbus.databinding.FragmentSearchInformationBinding
import rmnvich.apps.coursework.modbus.domain.entity.base.Sensor
import javax.inject.Inject

class SensorInformationFragment : Fragment(), SensorInformationContract.View {

    @Inject
    lateinit var mPresenter: SensorInformationPresenter

    private lateinit var mBinding: FragmentSearchInformationBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.instance.componentHolder
                .getSensorInformationComponent()
                ?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_search_information,
                container, false
        )

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        mPresenter.viewIsReady()
    }

    override fun displaySensorInfo(sensor: Sensor) {
        mBinding.sensor = sensor
        mBinding.invalidateAll()
    }

    override fun setTextViewsInformationColor(colors: Array<Int>) {
        mBinding.tvName.setTextColor(colors[0])
        mBinding.tvManufacturer.setTextColor(colors[0])
        mBinding.tvVersion.setTextColor(colors[0])
        mBinding.tvNetworkAddress.setTextColor(colors[0])
        mBinding.tvSerialNumber.setTextColor(colors[0])
        mBinding.tvIndications.setTextColor(colors[0])

        mBinding.tvDeviceStatus.setTextColor(colors[1])
    }

    override fun setDeviceConnectionStatus(isConnected: Boolean) {
        val colors = if (isConnected) {
            mBinding.tvDeviceStatus.text = getString(R.string.device_attached)
            arrayOf(ContextCompat.getColor(context!!, R.color.colorWhite),
                    ContextCompat.getColor(context!!, R.color.colorGreen))
        } else {
            mBinding.tvDeviceStatus.text = getString(R.string.device_detached)
            arrayOf(ContextCompat.getColor(context!!, R.color.colorWhiteAlpha),
                    ContextCompat.getColor(context!!, R.color.colorRed))
        }
        setTextViewsInformationColor(colors)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mBinding.progressBar.visibility = View.INVISIBLE
    }

    override fun onDetach() {
        super.onDetach()
        mPresenter.detachView()
        App.instance.componentHolder
                .releaseSensorInformationComponent()
    }
}