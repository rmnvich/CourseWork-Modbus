package rmnvich.apps.coursework.modbus.presentation.activity.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ftdi.j2xx.D2xxManager
import rmnvich.apps.coursework.modbus.R
import rmnvich.apps.coursework.modbus.presentation.fragment.sensorinfo.SensorInformationFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    //TODO: Request: 00 03 00 00 00 04 45 D8
    //TODO: Response: 01 03 08 00 01 00 03 12 E8 00 00 44 5B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.container, SensorInformationFragment.newInstance())
            .commit()
    }
}