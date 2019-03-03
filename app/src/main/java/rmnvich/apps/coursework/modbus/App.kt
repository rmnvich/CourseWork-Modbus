package rmnvich.apps.coursework.modbus

import android.app.Application
import rmnvich.apps.coursework.modbus.di.ComponentHolder
import timber.log.Timber

class App : Application() {

    lateinit var componentHolder: ComponentHolder

    override fun onCreate() {
        super.onCreate()
        instance = this

        componentHolder = ComponentHolder(applicationContext)
        componentHolder.init()

        Timber.plant(FileLoggingTree(applicationContext))
    }

    companion object {
        lateinit var instance: App
    }
}