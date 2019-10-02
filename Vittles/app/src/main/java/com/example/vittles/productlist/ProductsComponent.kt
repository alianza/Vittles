package com.example.vittles.productlist

import com.example.vittles.di.AppComponent
import com.example.vittles.di.CustomScope
import dagger.Component

@CustomScope
@Component(modules = [ProductsModule::class],
        dependencies = [AppComponent::class]
)
interface ProductsComponent {
    fun inject(view: ProductsActivity)
}