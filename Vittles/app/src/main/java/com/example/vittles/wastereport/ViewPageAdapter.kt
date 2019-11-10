package com.example.vittles.wastereport

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter internal constructor(fm: FragmentManager, var data: Int, val context: Context) : FragmentStatePagerAdapter(fm) {

    private val COUNT = 2
    lateinit var fragment: Fragment


    fun updatePercent(percentNew: Int) {
        data = percentNew
        notifyDataSetChanged()
    }

    override fun getItemPosition(obj: Any): Int {
        if (obj is RefreshData) {
            obj.refresh(data)
        }
        return super.getItemPosition(obj)
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> fragment = FragmentCircleChart(data, context)
            1 -> fragment = FragmentBarChart()
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
