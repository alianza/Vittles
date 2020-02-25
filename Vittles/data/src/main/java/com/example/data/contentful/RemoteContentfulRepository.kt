package com.example.data.contentful

import android.content.Context
import android.util.Log
import com.contentful.java.cda.CDAClient
import com.contentful.vault.*
import com.example.data.BuildConfig
import com.example.domain.termsandconditions.TermsAndConditions
import com.example.domain.termsandconditions.TermsAndConditionsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import com.example.data.contentful.`TermsAndConditionsResponse$Fields` as TermsAndConditionsResponseFields


class RemoteContentfulRepository @Inject constructor(
    private val context: Context
) : TermsAndConditionsRepository {

    private val client by lazy {
        CDAClient.builder()
            .setSpace(BuildConfig.ContentfulSpaceKey)
            .setToken(BuildConfig.ContentfulAPIKey)
            .setEnvironment(BuildConfig.ContentfulEnvironmentKey)
            .build()
    }

    private val syncConfig = SyncConfig.builder()
        .setClient(client)
        .build()

    private val vault by lazy {
        Vault.with(context, ContentSpace::class.java).apply {
            requestSync(syncConfig)
        }
    }

    override fun getTermsAndConditions(): Observable<TermsAndConditions> {
        val observeTerms = vault.observe(TermsAndConditionsResponse::class.java).all().firstElement().map {
            TermsAndConditions(
                it.title,
                it.content
            )
        }.toObservable()
        val observeSync = Vault.observeSyncResults().toObservable()
            .flatMap { observeTerms }
        return observeTerms
    }


    @Space(
        value = BuildConfig.ContentfulSpaceKey,
        models = [TermsAndConditionsResponse::class],
        locales = ["en"],
        dbVersion = VAULT_VERSION
    )
    inner class ContentSpace

    companion object {

        private const val TERMS_CONDITIONS_ID = "termsAndConditions"
        private const val VAULT_VERSION = 3
        private const val TAG = "VAULT"
    }
}