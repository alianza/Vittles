package com.example.vittles.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.domain.barcode.EmptyProductDictionary
import com.example.domain.settings.*
import com.example.domain.settings.model.NotificationSchedule
import com.example.vittles.mvp.BasePresenter
import com.example.vittles.services.notification.NotificationScheduleService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The presenter for the settings activity.
 *
 * @author Jan-Willem van Bremen
 *
 * @property setNotificationSchedule
 * @property setNotificationEnabled
 * @property setVibrationEnabled
 * @property getNotificationSchedule
 * @property getNotificationEnabled
 * @property getVibrationEnabled
 * @property emptyProductDictionary
 */
class SettingsPresenter @Inject constructor(
    private val setNotificationSchedule: SetNotificationSchedule,
    private val setNotificationEnabled: SetNotificationEnabled,
    private val setVibrationEnabled: SetVibrationEnabled,
    private val getNotificationSchedule: GetNotificationSchedule,
    private val getNotificationEnabled: GetNotificationEnabled,
    private val getVibrationEnabled: GetVibrationEnabled,
    private val emptyProductDictionary: EmptyProductDictionary
) : BasePresenter<SettingsFragment>(), SettingsContract.Presenter {

    /** TODO */
    private lateinit var context: Context
    /** TODO */
    val notificationEnabled = MutableLiveData<Boolean>(true)
    /** TODO */
    val notificationSchedule = MutableLiveData<NotificationSchedule>()
    /** TODO */
    val vibrationEnabled = MutableLiveData<Boolean>(true)

    fun startPresenting(context: Context) {
        this.context = context
        notificationEnabled.value = getNotificationEnabled()
        notificationSchedule.value = getNotificationSchedule()
        vibrationEnabled.value = getVibrationEnabled()
    }

    override fun onNotificationScheduleChanged(notificationSchedule: NotificationSchedule) {
        setNotificationSchedule(notificationSchedule)
        this.notificationSchedule.value = notificationSchedule
        NotificationScheduleService.scheduleNotificationAudit(
            context,
            getNotificationSchedule(),
            getNotificationEnabled()
        )
    }

    fun onNotificationEnabledChanged(isEnabled: Boolean) {
        setNotificationEnabled(isEnabled)
        this.notificationEnabled.value = isEnabled
        if (getNotificationEnabled()) {
            NotificationScheduleService.scheduleNotificationAudit(
                context,
                getNotificationSchedule(),
                getNotificationEnabled()
            )
        } else {
            NotificationScheduleService.exitNotificationSchedule(context)
        }
    }

    fun onVibrationEnabledChanged(isEnabled: Boolean) {
        setVibrationEnabled(isEnabled)
        this.vibrationEnabled.value = isEnabled
    }

    /**
     * Method used to remove all local product names
     *
     */
    override fun clearProductDictionary() {
        disposables.add(
            emptyProductDictionary().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.onProductDictionaryClearSuccess() }, { view?.onProductDictionaryClearFail() })
        )
    }
}