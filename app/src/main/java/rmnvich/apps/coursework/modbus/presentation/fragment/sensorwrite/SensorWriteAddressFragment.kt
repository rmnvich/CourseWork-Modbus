package rmnvich.apps.coursework.modbus.presentation.fragment.sensorwrite

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rmnvich.apps.coursework.modbus.App
import javax.inject.Inject

class SensorWriteAddressFragment : Fragment(), SensorWriteAddressContract.View {

    @Inject
    lateinit var mPresenter: SensorWriteAddressPresenter

    companion object {
        fun newInstance(): SensorWriteAddressFragment {
            return SensorWriteAddressFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.instance.componentHolder
            .getSensorWriteAddressComponent()
            ?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        mPresenter.viewIsReady()
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onDetach() {
        super.onDetach()
        App.instance.componentHolder
            .releaseSensorWriteAddressComponent()
    }
}