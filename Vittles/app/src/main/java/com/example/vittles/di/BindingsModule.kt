package com.example.vittles.di

import com.example.vittles.reports.ReportsFragment
import com.example.vittles.productadd.AddProductFragment
import com.example.vittles.productlist.ProductListFragment
import com.example.vittles.scanning.ScannerFragment
import com.example.vittles.services.notification.NotificationScheduleService
import com.example.vittles.wastereport.WasteReportActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingsModule {
    @ContributesAndroidInjector
    abstract fun bindProductListFragment(): ProductListFragment

    @ContributesAndroidInjector
    abstract fun bindAddProductFragment(): AddProductFragment

    @ContributesAndroidInjector
    abstract fun bindScannerFragment(): ScannerFragment

    @ContributesAndroidInjector
    abstract fun bindNotificationScheduleService(): NotificationScheduleService

    @ContributesAndroidInjector
    abstract fun bindReportsFragment(): ReportsFragment

    @ContributesAndroidInjector
    abstract fun bindWasteReportActivity(): WasteReportActivity
}