package com.example.data.contentful

import com.contentful.java.cda.CDAClient
import com.contentful.java.cda.CDAEntry
import com.example.data.BuildConfig
import com.example.domain.termsandconditions.TermsAndConditions
import com.example.domain.termsandconditions.TermsAndConditionsRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RemoteContentfulRepository @Inject constructor() : TermsAndConditionsRepository {

    private val client by lazy {
        CDAClient.builder()
            .setSpace(BuildConfig.ContentfulSpaceKey)
            .setToken(BuildConfig.ContentfulAPIKey)
            .build()
    }

    override fun getTermsAndConditions(): Observable<TermsAndConditions> {
        return Observable.defer {
            client.observe(CDAEntry::class.java)
                .withContentType(TC_ID)
                .one(TC_ENTRY_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable().map {
                    TermsAndConditions(
                        it.getField("title"),
                        it.getField("content")
                    )
                }
        }
    }

    companion object {

        private const val TC_ID = "termsAndConditions"
        private const val TC_ENTRY_ID = "Hf6eZRoPix2kYfMnNVVXl"
    }
}