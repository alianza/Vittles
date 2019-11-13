package com.example.vittles.wastereport

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.vittles.R
import com.example.vittles.enums.TimeRange
import kotlinx.android.synthetic.main.popup_report_time_range.view.*
import org.joda.time.DateTime

/**
 * Class for the sorting Menu, This menu shows all the sorting types and the current sorting type.
 *
 * @author Marc van Spronsen
 * @author Sarah Lange
 *
 * @property currentTimeRange The current sorting type that is used.
 * @property previousTimeRange The previous sorting type that was used.
 * @property btnTimeRange The button which shows the current sortingType.
 * @property alertDialog The entire alertDialog of the sortMenu.
 * @property view The View which holds the sortingMenu.
 */
class WasteTimeRangeMenu (private var presenter: WasteReportPresenter, private val onDateChange: (DateTime) -> Unit) {

    private var currentTimeRange: TimeRange = TimeRange.LAST_SEVEN_DAYS
    lateinit var previousTimeRange: TimeRange
    lateinit var btnTimeRange: TextView
    lateinit var alertDialog: AlertDialog
    lateinit var view: View

    /**
     * Sets enums for all of the different sorting options
     *
     */
    //enum class SortingType {
    //    LAST_7_DAYS,
     //   LAST_30_DAYS,
      //  LAST_YEAR
    //}

    /**
     * Inflates the sortingMenu.
     *
     * @param context The context in which the sortingMenu is active.
     * @param button The button which shows the current sortingType.
     */
    fun openMenu(context: Context, button: TextView) {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.popup_report_time_range, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(mDialogView)
        val  mAlertDialog = mBuilder.show()

        btnTimeRange = button
        alertDialog = mAlertDialog
        view = mDialogView

        setCircleColor()
        setListeners()
    }

    /**
     * Sets all the onClickListeners
     *
     */
    private fun setListeners() {
        view.daysRemainingLH.setOnClickListener { onSortClick(TimeRange.LAST_SEVEN_DAYS)  }
        view.daysRemainingHL.setOnClickListener { onSortClick(TimeRange.LAST_30_DAYS) }
        view.alfabeticAZ.setOnClickListener { onSortClick(TimeRange.LAST_YEAR) }
    }

    /**
     * Handles all actions that happen when a button is clicked
     *
     */
    private fun onSortClick(timeRange: TimeRange) {
        when(timeRange) {
            TimeRange.LAST_SEVEN_DAYS -> loadData(TimeRange.LAST_SEVEN_DAYS.value)
            TimeRange.LAST_30_DAYS -> loadData(TimeRange.LAST_30_DAYS.value)
            TimeRange.LAST_YEAR -> loadData(TimeRange.LAST_YEAR.value)
        }

        previousTimeRange = currentTimeRange
        currentTimeRange = timeRange

        if (currentTimeRange != previousTimeRange) {
            setSortbtnText(timeRange)
        }

        if (::alertDialog.isInitialized) {
            alertDialog.dismiss()
        }
    }

    private fun loadData(date: DateTime) {
        onDateChange(date)
        //presenter.getCountEatenProducts(DateTime.now().minusDays(days))
        //presenter.getCountExpiredProducts(DateTime.now().minusDays(days))
        //presenter.getPercent(DateTime.now().minusDays(days))
    }

    /**
     * Sets the circle alpha to 1 of the selected sorting method.
     *
     */
    private fun setCircleColor() {
        when (currentTimeRange) {
            TimeRange.LAST_SEVEN_DAYS -> view.daysRemainingAsc.alpha = 1f
            TimeRange.LAST_30_DAYS-> view.daysRemainingDesc.alpha = 1f
            TimeRange.LAST_YEAR -> view.alfabeticAz.alpha = 1f
        }
    }

    /**
     * Sets the buttonText to the current sorting method.
     *
     * @param sortingType The sortingType which was selected by the user.
     */
    private fun setSortbtnText(sortingType: TimeRange) {
        when(sortingType) {
            TimeRange.LAST_SEVEN_DAYS -> btnTimeRange.text = view.daysRemainingLH.text
            TimeRange.LAST_30_DAYS -> btnTimeRange.text = view.daysRemainingHL.text
            TimeRange.LAST_YEAR -> btnTimeRange.text = view.alfabeticAZ.text
        }
    }
}