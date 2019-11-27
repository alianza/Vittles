package com.example.vittles.di

import com.example.vittles.settings.SettingsFragment
import com.example.vittles.reports.ReportsFragment
import com.example.vittles.productlist.ProductListFragment
import com.example.vittles.productlist.productinfo.ProductInfoFragment
import com.example.vittles.scanning.ScannerFragment
import com.example.vittles.services.notification.NotificationScheduleService
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
    abstract fun bindReportsFragment(): ReportsFragment

    @ContributesAndroidInjector
    abstract  fun bindProductInfoFragment(): ProductInfoFragment

    @ContributesAndroidInjector
    abstract fun bindSettingsFragment(): SettingsFragment
}