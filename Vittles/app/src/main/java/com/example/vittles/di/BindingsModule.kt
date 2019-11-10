package com.example.vittles.di

import com.example.vittles.productadd.AddProductActivity
import com.example.vittles.productlist.ProductsActivity
import com.example.vittles.services.notification.NotificationScheduleService
import com.example.vittles.wastereport.WasteReportActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingsModule {
    @ContributesAndroidInjector
    abstract fun bindProductsActivity(): ProductsActivity

    @ContributesAndroidInjector
    abstract fun bindAddProductActivity(): AddProductActivity

    @ContributesAndroidInjector
    abstract fun bindNotificationScheduleService(): NotificationScheduleService

    @ContributesAndroidInjector
    abstract fun bindWasteReportActivity(): WasteReportActivity
}