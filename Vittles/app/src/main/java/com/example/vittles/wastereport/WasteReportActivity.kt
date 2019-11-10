package com.example.vittles.wastereport

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.vittles.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_waste_report.*
import kotlinx.android.synthetic.main.content_waste_history.*
import org.joda.time.DateTime
import javax.inject.Inject

class WasteReportActivity : DaggerAppCompatActivity(), WasteReportContract.View  {

    @Inject
    lateinit var presenter: WasteReportPresenter

    lateinit var timeRangeMenu: WasteTimeRangeMenu
    lateinit var adapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_report)

        timeRangeMenu = WasteTimeRangeMenu(presenter)
        presenter.start(this)
        presenter.getCountEatenProducts(DateTime.now().minusDays(1) )
        presenter.getCountExpiredProducts(DateTime.now().minusDays(1))
        presenter.getPercent(DateTime.now().minusDays(1))

        time_Range.setOnClickListener { showTimeRangeSelector() }

    }

    override fun showTimeRangeSelector() {
        timeRangeMenu.openMenu(this, btnSort)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showEatenProducts(eatenProducts: Int) {
        tvVittlesEaten.text = eatenProducts.toString()
    }


    override fun showExpiredProducts(expiredProducts: Int) {
        tvVittlesExpired.text = expiredProducts.toString()
    }

    override fun setNoResultsView() {
        Toast.makeText(applicationContext, R.string.count_fail, Toast.LENGTH_SHORT).show()
    }

    override fun calculateWaste(percent: Int) {
        if(view_pager.adapter == null) {
            loadPageView(percent)
        } else {
            adapter.updatePercent(percent)
            loadPageView(percent)
        }

    }

    override fun loadPageView(percent: Int) {
        adapter = ViewPagerAdapter(supportFragmentManager, percent, this)
        view_pager.adapter = adapter

        view_pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                if(position == 0) {
                    findViewById<ImageView>(R.id.ivDot2).setImageDrawable(ContextCompat.getDrawable(this@WasteReportActivity, R.drawable.dot))
                    findViewById<ImageView>(R.id.ivDot1).setImageDrawable(ContextCompat.getDrawable(this@WasteReportActivity, R.drawable.dot_selected))
                }
                if(position == 1) {
                    findViewById<ImageView>(R.id.ivDot1).setImageDrawable(ContextCompat.getDrawable(this@WasteReportActivity, R.drawable.dot))
                    findViewById<ImageView>(R.id.ivDot2).setImageDrawable(ContextCompat.getDrawable(this@WasteReportActivity, R.drawable.dot_selected))
                }

            }

        })
    }

}
