package rmnvich.apps.coursework.modbus.presentation.fragment.viewpager

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import rmnvich.apps.coursework.modbus.R
import rmnvich.apps.coursework.modbus.databinding.ViewPagerFragmentBinding
import rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo.SensorInformationFragment
import rmnvich.apps.coursework.modbus.presentation.fragment.sensorwrite.SensorWriteAddressFragment

class SensorViewPagerFragment : Fragment() {

    private lateinit var mBinding: ViewPagerFragmentBinding

    private lateinit var mAdapter: FragmentPagerItemAdapter

    companion object {
        fun newInstance(): SensorViewPagerFragment {
            return SensorViewPagerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.view_pager_fragment,
                container, false)

        mAdapter = FragmentPagerItemAdapter(
                childFragmentManager, FragmentPagerItems.with(context)
                .add(R.string.information, SensorInformationFragment::class.java)
                .add(R.string.addresses, SensorWriteAddressFragment::class.java)
                .create()
        )

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.sensorsViewpager.adapter = mAdapter
        mBinding.viewpagertab.setViewPager(mBinding.sensorsViewpager)
    }
}