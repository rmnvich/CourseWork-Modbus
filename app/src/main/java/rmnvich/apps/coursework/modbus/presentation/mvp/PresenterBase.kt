package rmnvich.apps.coursework.modbus.presentation.mvp

import android.app.Activity
import android.support.v4.app.Fragment

abstract class PresenterBase<T : MvpView> : MvpPresenter<T> {

    var view: T? = null
        private set

    override fun attachView(mvpView: T) {
        view = mvpView
    }

    override fun detachView() {
        view = null
    }

    fun getString(resourceId: Int): String {
        return try {
            (view as Activity).getString(resourceId)
        } catch (e: ClassCastException) {
            (view as Fragment).getString(resourceId)
        }
    }
}