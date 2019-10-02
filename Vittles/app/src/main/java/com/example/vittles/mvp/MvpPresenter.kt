package com.example.vittles.mvp

/**
 * Interface of the BasePresenter.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 */
interface MvpPresenter {
    /**
     * Stop presenter.
     *
     */
    fun stop()

    /**
     * Destroy content of presenter.
     *
     */
    fun destroy()
}