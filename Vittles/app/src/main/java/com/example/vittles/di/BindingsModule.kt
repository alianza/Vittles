package com.example.vittles.di

import com.example.vittles.Reports.ReportsFragment
import com.example.vittles.productadd.AddProductFragment
import com.example.vittles.productlist.ProductsFragment
import com.example.vittles.services.notification.NotificationScheduleService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingsModule {
    @ContributesAndroidInjector
    abstract fun bindProductsActivity(): ProductsFragment

    @ContributesAndroidInjector
    abstract fun bindAddProductActivity(): AddProductFragment

    @ContributesAndroidInjector
    abstract fun bindNotificationScheduleService(): NotificationScheduleService

    @ContributesAndroidInjector
    abstract fun bindReportsFragment(): ReportsFragment
}