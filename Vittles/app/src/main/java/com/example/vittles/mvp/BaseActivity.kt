package com.example.vittles.mvp

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    val inject by lazy { injectDependencies() }

    protected abstract fun injectDependencies()
}