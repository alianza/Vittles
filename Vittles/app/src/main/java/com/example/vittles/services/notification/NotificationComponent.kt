package com.example.vittles.services.notification

import com.example.vittles.di.AppComponent
import com.example.vittles.di.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [NotificationModule::class],
    dependencies = [AppComponent::class]
)
interface NotificationComponent {

    fun inject(receiver: NotificationScheduleService)
}