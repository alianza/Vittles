package com.example.vittles.productadd

import com.example.vittles.di.AppComponent
import com.example.vittles.di.CustomScope
import dagger.Component
import dagger.Provides

@CustomScope
@Component(modules = [AddProductModule::class],
    dependencies = [AppComponent::class]
)
interface AddProductComponent {
    fun inject(view: AddProductActivity)
}