package com.example.vittles.productlist

import com.example.vittles.di.AppComponent
import com.example.vittles.di.CustomScope
import com.example.vittles.productlist.MainActivity
import dagger.Component
import dagger.Provides

@CustomScope
@Component(modules = [ProductsModule::class],
        dependencies = [AppComponent::class]
)
interface ProductsComponent {
    fun inject(view: MainActivity)
}