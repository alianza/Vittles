package com.example.vittles.wastereport

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.vittles.R
import com.example.vittles.enums.TimeRange
import com.example.vittles.services.popups.SingleChoiceMenu
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_waste_history.*
import kotlinx.android.synthetic.main.fragment_waste_report.*
import kotlinx.coroutines.*
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * @author Sarah Lange
 *
 */
class WasteReportFragment : DaggerFragment(), WasteReportContract.View {

    @Inject
    lateinit var presenter: WasteReportPresenter

    private lateinit var adapter: ViewPagerAdapter
    private var currentTimeRange = TimeRange.LAST_SEVEN_DAYS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_waste_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start(this)
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun initData() {
        // Call get methods asynchronously, then call initViews synchronously
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val vittlesEaten =
                    async { presenter.getCountEatenProducts(TimeRange.LAST_SEVEN_DAYS.date) }
                val vittlesExpired =
                    async { presenter.getCountExpiredProducts(TimeRange.LAST_SEVEN_DAYS.date) }

                initViews(vittlesEaten.await(), vittlesExpired.await())
            }
        }
    }

    override fun initViews(vittlesEaten: Int, vittlesExpired: Int) {
        // Inside handler because android doesn't allow UI changes outside main thread.
        Handler(Looper.getMainLooper()).post {
            adapter = ViewPagerAdapter(
                activity!!.supportFragmentManager,
                TimeRange.LAST_SEVEN_DAYS.date,
                vittlesEaten,
                vittlesExpired
            )
            viewPager.adapter = adapter
            addOnPageChangeListener()

            showEatenVittles(vittlesEaten)
            showExpiredVittles(vittlesExpired)
        }
        timeRange.setOnClickListener { showTimeRangeSelector() }
        changeDate(TimeRange.LAST_SEVEN_DAYS.date)
    }

    override fun showTimeRangeSelector() {
        context?.let {
            val provider = TimeRangeTextProvider(requireContext())
            SingleChoiceMenu(
                provider,
                { option: TimeRange ->
                    changeDate(option.date)
                    tvTimeRange.text = provider.getText(option)
                    currentTimeRange = option
                },
                currentTimeRange,
                TimeRange.values(),
                R.string.time_range
            ).show(requireFragmentManager(), TAG)
        }
    }

    override fun changeDate(date: DateTime) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val vittlesEaten =
                    async { presenter.getCountEatenProducts(date) }
                val vittlesExpired =
                    async { presenter.getCountExpiredProducts(date) }
                showChangeDate(vittlesEaten.await(), vittlesExpired.await(), date)
            }
        }
    }

    override fun showChangeDate(vittlesEaten: Int, vittlesExpired: Int, date: DateTime) {
        Handler(Looper.getMainLooper()).post {
            showEatenVittles(vittlesEaten)
            showExpiredVittles(vittlesExpired)
            adapter.updateDate(date, vittlesEaten, vittlesExpired)
        }
    }

    override fun showEatenVittles(eatenVittles: Int) {
        tvVittlesEaten.text = eatenVittles.toString()
    }

    override fun showExpiredVittles(expiredVittles: Int) {
        tvVittlesExpired.text = expiredVittles.toString()
    }

    override fun setNoResultsView() {
        Toast.makeText(context, R.string.count_fail, Toast.LENGTH_SHORT).show()
    }

    override fun addOnPageChangeListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    ivDotRight.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.dot
                        )
                    })
                    ivDotLeft.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.dot_selected
                        )
                    })
                } else if (position == 1) {
                    ivDotLeft.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.dot
                        )
                    })
                    ivDotRight.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.dot_selected
                        )
                    })
                }

            }

        })
    }

    companion object {

        private const val TAG = "WASTE_REPORT_FRAGMENT"
    }
}
