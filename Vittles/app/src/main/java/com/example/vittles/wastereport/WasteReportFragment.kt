package com.example.vittles.wastereport

import android.media.VolumeShaper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.enums.TimeRange
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_waste_report.*
import kotlinx.android.synthetic.main.content_waste_history.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.intellij.lang.annotations.Flow
import org.joda.time.DateTime
import org.joda.time.MutableDateTime
import java.util.*
import javax.inject.Inject

class WasteReportFragment : DaggerFragment(), WasteReportContract.View {

    @Inject
    lateinit var presenter: WasteReportPresenter

    lateinit var timeRangeMenu: WasteTimeRangeMenu
    lateinit var tvVittlesEaten: TextView
    lateinit var tvVittlesExpired: TextView
    lateinit var ivDot: ImageView
    lateinit var ivDot2: ImageView
    lateinit var timeRange: ConstraintLayout
    lateinit var view_pager: ViewPager

    lateinit var adapter: ViewPagerAdapter
    var vittlesEaten: Int = 0
    var vittlesExpired: Int = 0
    var eatenLoaded = false
    var expiredLoaded = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        timeRangeMenu = WasteTimeRangeMenu(presenter) { date -> changeDate(date) }
        return inflater.inflate(R.layout.fragment_waste_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvVittlesEaten = view.findViewById(R.id.tvVittlesEaten)
        tvVittlesExpired = view.findViewById(R.id.tvVittlesExpired)
        ivDot = view.findViewById(R.id.ivDot1)
        ivDot2 = view.findViewById(R.id.ivDot2)
        timeRange = view.findViewById(R.id.time_Range)
        view_pager = view.findViewById(R.id.view_pager)


        presenter.start(this)
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                presenter.getCountEatenProducts(DateTime.now().minusDays(1))
                presenter.getCountExpiredProducts(DateTime.now().minusDays(1))
            }
            initViews()
        }



        //presenter.getPercent(DateTime.now().minusDays(1))

    }


    override fun initViews() {
        adapter = ViewPagerAdapter(
            activity!!.supportFragmentManager,
            TimeRange.LAST_SEVEN_DAYS.value,
            vittlesEaten,
            vittlesExpired
        )
        view_pager.adapter = adapter
        addOnPageChangeListener()


        timeRange.setOnClickListener { showTimeRangeSelector() }
    }

    override fun showTimeRangeSelector() {
        context?.let { timeRangeMenu.openMenu(it, btnSort) }
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun changeDate(date: DateTime) {
        presenter.getCountEatenProducts(date)
        presenter.getCountExpiredProducts(date)
        adapter.updateDate(date)
    }

    override fun showEatenProducts(eatenProducts: Int) {
        vittlesEaten = eatenProducts
        tvVittlesEaten.text = eatenProducts.toString()
        eatenLoaded = true
        //initViews()
    }


    override fun showExpiredProducts(expiredProducts: Int) {
        vittlesExpired = expiredProducts
        tvVittlesExpired.text = expiredProducts.toString()
        expiredLoaded = true
        //initViews()
    }

    override fun setNoResultsView() {
        Toast.makeText(context, R.string.count_fail, Toast.LENGTH_SHORT).show()
    }

    override fun calculateWaste(percent: Int) {
        if (view_pager.adapter == null) {
            //loadPageView(percent)
        } else {
            //adapter.updatePercent(percent)
            //loadPageView(percent)
        }

    }

    override fun addOnPageChangeListener() {
        view_pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

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
                    ivDot2.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.dot
                        )
                    })
                    ivDot.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.dot_selected
                        )
                    })
                }
                if (position == 1) {
                    ivDot.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.dot
                        )
                    })
                    ivDot2.setImageDrawable(context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.dot_selected
                        )
                    })
                }

            }

        })
    }


}
