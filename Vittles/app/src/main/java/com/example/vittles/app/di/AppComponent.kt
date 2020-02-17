package com.example.vittles.app.di

import com.example.vittles.app.VittlesApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/** @suppress */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        BindingsModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<VittlesApp> {

    override fun inject(application: VittlesApp)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun applicationBind(application: VittlesApp): Builder

        fun appModule(appModule: AppModule): Builder
    }
}
