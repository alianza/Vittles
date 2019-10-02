package com.example.vittles.mvp

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    val inject by lazy { injectDependencies() }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        inject
        return super.onCreateView(parent, name, context, attrs)
    }

    protected abstract fun injectDependencies()
}