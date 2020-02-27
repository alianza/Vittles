@file:Suppress("unused")

package com.example.vittles.app.di

import com.example.vittles.dashboard.productinfo.ProductInfoFragment
import com.example.vittles.dashboard.productlist.ProductListFragment
import com.example.vittles.dashboard.productlist.ui.toolbar.ProductListToolbarSortMenu
import com.example.vittles.scanning.ScannerFragment
import com.example.vittles.services.notification.NotificationScheduleService
import com.example.vittles.settings.SettingsFragment
import com.example.vittles.termsandconditions.TermsAndConditionsActivity
import com.example.vittles.wastereport.WasteReportFragment
import com.example.vittles.wastereport.barchart.BarChartFragment
import com.example.vittles.wastereport.circlechart.CircleChartFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BindingsModule {
    @ContributesAndroidInjector
    fun bindProductListFragment(): ProductListFragment

    @ContributesAndroidInjector
    fun bindScannerFragment(): ScannerFragment

    @ContributesAndroidInjector
    fun bindNotificationScheduleService(): NotificationScheduleService

    @ContributesAndroidInjector
    fun bindProductInfoFragment(): ProductInfoFragment

    @ContributesAndroidInjector
    fun bindSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    fun bindWasteReportFragment(): WasteReportFragment

    @ContributesAndroidInjector
    fun bindBarChartFragment(): BarChartFragment

    @ContributesAndroidInjector
    fun bindCircleChartFragment(): CircleChartFragment

    @ContributesAndroidInjector
    fun bindTermsAndConditionsActivity(): TermsAndConditionsActivity
}