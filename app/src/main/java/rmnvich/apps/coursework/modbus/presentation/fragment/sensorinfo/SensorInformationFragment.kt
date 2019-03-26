package rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.florent37.fiftyshadesof.FiftyShadesOf
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
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_search_information,
                container, false)
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

    override fun hidePlaceholders() {
        setTextViewsColor(ContextCompat.getColor(context!!, R.color.colorWhite))
        mBinding.placeholderGroup.finishAnimation()
    }

    override fun setTextViewsColor(color: Int) {
        Handler().postDelayed({
            mBinding.tvName.setTextColor(color)
            mBinding.tvManufacturer.setTextColor(color)
            mBinding.tvVersion.setTextColor(color)
            mBinding.tvIndications.setTextColor(color)
        }, 500)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDetach() {
        super.onDetach()
        App.instance.componentHolder
                .releaseSensorInformationComponent()
    }
}