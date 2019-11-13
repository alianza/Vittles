package com.example.vittles.wastereport

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.vittles.wastereport.BarChart.BarChartFragment
import com.example.vittles.wastereport.CircleChart.CircleChartFragment
import com.example.vittles.wastereport.CircleChart.RefreshData
import org.joda.time.DateTime

class ViewPagerAdapter internal constructor(
    fm: FragmentManager,
    var date: DateTime,
    var vittlesEaten: Int,
    var vittlesExpired: Int
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val COUNT = 2
    lateinit var fragment: Fragment


    fun updateDate(date: DateTime) {
        this.date = date
        notifyDataSetChanged()
    }

    override fun getItemPosition(obj: Any): Int {
        if (obj is RefreshData) {
            obj.refresh(date)
        }
        return super.getItemPosition(obj)
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> { fragment = CircleChartFragment(date, vittlesEaten, vittlesExpired) }
            1 -> fragment = BarChartFragment(date)
        }
        return fragment
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab " + (position + 1)
    }
}
