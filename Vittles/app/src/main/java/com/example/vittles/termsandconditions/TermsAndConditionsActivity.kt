package com.example.vittles.termsandconditions

import android.os.Bundle
import com.example.domain.termsandconditions.TermsAndConditions
import com.example.vittles.R
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


}
