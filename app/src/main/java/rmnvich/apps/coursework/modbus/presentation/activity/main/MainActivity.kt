package rmnvich.apps.coursework.modbus.presentation.activity.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ftdi.j2xx.D2xxManager
import rmnvich.apps.coursework.modbus.R
import rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo.SensorInformationFragment
import rmnvich.apps.coursework.modbus.presentation.fragment.viewpager.SensorViewPagerFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                .add(R.id.container, SensorViewPagerFragment.newInstance())
                .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        System.exit(0)
    }
}