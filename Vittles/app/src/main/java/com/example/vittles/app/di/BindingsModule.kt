package com.example.vittles.app.di

import com.example.vittles.dashboard.productinfo.ProductInfoFragment
import com.example.vittles.dashboard.productlist.ProductListFragment
import com.example.vittles.dashboard.productlist.ui.toolbar.ProductListToolbar
import com.example.vittles.dashboard.productlist.ui.toolbar.ProductListToolbarSortMenu
import com.example.vittles.scanning.ScannerFragment
import com.example.vittles.services.notification.NotificationScheduleService
import com.example.vittles.settings.SettingsFragment
import com.example.vittles.wastereport.WasteReportFragment
import com.example.vittles.wastereport.barchart.BarChartFragment
import com.example.vittles.wastereport.circlechart.CircleChartFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/** @suppress */
@Module
abstract class BindingsModule {
    @ContributesAndroidInjector
    abstract fun bindProductListFragment(): ProductListFragment

    @ContributesAndroidInjector
    abstract fun bindScannerFragment(): ScannerFragment

    @ContributesAndroidInjector
    abstract fun bindNotificationScheduleService(): NotificationScheduleService

    @ContributesAndroidInjector
    abstract fun bindProductInfoFragment(): ProductInfoFragment

    @ContributesAndroidInjector
    abstract fun bindSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun bindWasteReportFragment(): WasteReportFragment

    @ContributesAndroidInjector
    abstract fun bindBarChartFragment(): BarChartFragment

    @ContributesAndroidInjector
    abstract fun bindCircleChartFragment(): CircleChartFragment

    @ContributesAndroidInjector
    abstract fun bindProductListSortMenu(): ProductListToolbarSortMenu
}