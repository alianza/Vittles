package com.example.vittles.mvp

import androidx.appcompat.app.AppCompatActivity

/**
 * Abstract superclass for all activities.
 *
 * @author Arjen Simons
 * @author Jeroen Flietstra
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * Lazy call the injectDependencies method.
     */
    val inject by lazy { injectDependencies() }

    /**
     * Abstract method which has to be implemented for the DaggerComponent that will build the
     * dependency injections.
     *
     */
    protected abstract fun injectDependencies()
}