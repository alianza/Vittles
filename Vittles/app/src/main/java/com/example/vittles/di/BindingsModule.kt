package com.example.vittles.di

import com.example.vittles.productadd.AddProductFragment
import com.example.vittles.productlist.ProductListFragment
import com.example.vittles.scanning.ScannerFragment
import com.example.vittles.services.notification.NotificationScheduleService
import com.example.vittles.wastereport.barchart.BarChartFragment
import com.example.vittles.wastereport.circlechart.CircleChartFragment
import com.example.vittles.wastereport.WasteReportFragment
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
    abstract fun bindWasteReportFragment(): WasteReportFragment

    @ContributesAndroidInjector
    abstract fun bindBarChartFragment(): BarChartFragment

    @ContributesAndroidInjector
    abstract fun bindCircleChartFragment(): CircleChartFragment
}