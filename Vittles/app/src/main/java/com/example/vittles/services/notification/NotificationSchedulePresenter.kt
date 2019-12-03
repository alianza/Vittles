package com.example.vittles.services.notification

import android.content.Context
import com.example.domain.notification.GetNotificationProductsExpired
import com.example.domain.notification.Notification
import com.example.domain.settings.*
import com.example.domain.settings.model.NotificationSchedule
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * TODO
 *
 * @property getNotificationSchedule
 * @property getNotificationEnabled
 * @property getNotification
 */
class NotificationSchedulePresenter @Inject constructor(
    private val getNotificationSchedule: GetNotificationSchedule,
    private val getNotificationEnabled: GetNotificationEnabled,
    private val getNotification: GetNotificationProductsExpired

) : BasePresenter<NotificationScheduleService>(), NotificationScheduleContract.Presenter {

    /**
     * TODO
     *
     * @param context
     */
    override fun startPresenting(context: Context) {
        disposables.add(
            getNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.notify(it, context)
                    schedule(context)
                }, {
                    view?.onNotifyFail(it)
                    schedule(context)
                })
        )
    }

    /**
     * TODO
     *
     * @param context
     */
    private fun schedule(context: Context) {
        NotificationScheduleService.scheduleNotificationAudit(
            context,
            getNotificationSchedule(),
            getNotificationEnabled()
        )
    }
}