package com.example.vittles.mvp

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

/**
 * Superclass of all presenters. This class has properties that will be used in all
 * presenters.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @param V The activity (V: View) of the presenter.
 */
abstract class BasePresenter<V : AppCompatActivity> : MvpPresenter {

    protected val disposables: CompositeDisposable = CompositeDisposable()
    protected var view: V? = null
        private set

    /**
     * Method to start the presenter, will assign the activity.
     *
     * @param view The activity (V: View) of the presenter.
     */
    fun start(view: V) {
        this.view = view
    }

    /**
     * Method to stop the presenter.
     *
     */
    override fun stop() {
        this.view = null
    }

    /**
     * Method to clear the disposables of the presenter.
     *
     */
    override fun destroy() {
        disposables.clear()
    }
}