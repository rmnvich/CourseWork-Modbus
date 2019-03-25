package rmnvich.apps.coursework.modbus.presentation.fragment.sensorwrite

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rmnvich.apps.coursework.modbus.App
import rmnvich.apps.coursework.modbus.R
import rmnvich.apps.coursework.modbus.databinding.FragmentWriteAddressBinding
import javax.inject.Inject

class SensorWriteAddressFragment : Fragment(), SensorWriteAddressContract.View {

    @Inject
    lateinit var mPresenter: SensorWriteAddressPresenter

    lateinit var mBinding: FragmentWriteAddressBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.instance.componentHolder
                .getSensorWriteAddressComponent()
                ?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_write_address,
                container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
        mPresenter.viewIsReady()
    }

    override fun showProgress() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun hideProgress() {
        mBinding.progressBar.visibility = View.INVISIBLE
    }

    override fun onDetach() {
        super.onDetach()
        App.instance.componentHolder
                .releaseSensorWriteAddressComponent()
    }
}