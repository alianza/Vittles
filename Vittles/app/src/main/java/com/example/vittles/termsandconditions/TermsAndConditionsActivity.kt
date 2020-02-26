package com.example.vittles.termsandconditions

import android.os.Bundle
import android.view.MenuItem
import com.example.domain.termsandconditions.TermsAndConditions
import com.example.vittles.R
import com.example.vittles.extension.setGone
import com.example.vittles.extension.setVisible
import com.m2mobi.markymarkandroid.MarkyMarkAndroid
import com.m2mobi.markymarkcontentful.ContentfulFlavor
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import javax.inject.Inject

class TermsAndConditionsActivity : DaggerAppCompatActivity(), TermsAndConditionsContract.View {

    @Inject
    lateinit var presenter: TermsAndConditionsPresenter

    private val markyMark by lazy {
        MarkyMarkAndroid.getMarkyMark(this, ContentfulFlavor()) { _, _ ->  }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        initToolbar()
        presenter.run {
            start(this@TermsAndConditionsActivity)
            onInitialize()
        }
    }



    override fun showTermsAndConditions(termsAndConditions: TermsAndConditions) {
        termsAndConditionsTextContainer.removeAllViews()
        markyMark.parseMarkdown(termsAndConditions.content).forEach {
            termsAndConditionsTextContainer.addView(it)
        }
    }

    override fun showLoadingView() {
        termsAndConditionsLoadingView.setVisible()
    }

    override fun hideLoadingView() {
        termsAndConditionsLoadingView.setGone()
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.title = getString(R.string.settings_terms_and_conditions)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }


}
