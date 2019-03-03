package rmnvich.apps.coursework.modbus.presentation.mvp

interface MvpPresenter<V : MvpView> {

    fun attachView(mvpView: V)

    fun viewIsReady()

    fun detachView()
}