package com.example.vittles.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.ActionMenuView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.size
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.vittles.dashboard.productlist.ProductListFragment
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.content_main.*
import androidx.navigation.findNavController as findNavSetup
import android.content.Intent
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import com.example.vittles.NavigationGraphDirections
import com.example.vittles.R
import com.example.vittles.enums.PreviousFragmentIndex
import com.example.vittles.settings.SettingsFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var navController: NavController

    /** All the top-level destinations of the application **/
    private val topLevelDestinations =
        arrayOf(
            R.id.productListFragment,
            R.id.wasteReportFragment,
            R.id.settingsFragment
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar) // Finish splash screen
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        initViews()
    }

    private fun initViews() {
        initNavigation()
        setListeners()
    }

    fun createErrorToast() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).apply {
            view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.red))
            view.findViewById<TextView>(android.R.id.message)?.setTextColor(Color.WHITE)
            show()
        }
    }

    private fun initNavigation() {
        navController = findNavSetup(R.id.fragmentHost)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        // Make reports top-level so that the back button disables
        appBarConfiguration.topLevelDestinations.addAll(topLevelDestinations)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        // Initialize navigation visibility
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productListFragment -> {
                    showBottomNavigationBar(barVisibility = true, fabVisibility = true)
                    getMenuItemByTitle(R.string.menu_home)?.let { setMenuItemIconColor(it) }
                }
                R.id.wasteReportFragment -> {
                    showBottomNavigationBar(barVisibility = true, fabVisibility = true)
                    getMenuItemByTitle(R.string.menu_reports)?.let { setMenuItemIconColor(it) }
                }
                R.id.scannerFragment -> {
                    showBottomNavigationBar(barVisibility = false, fabVisibility = false)
                }
                R.id.settingsFragment -> {
                    showBottomNavigationBar(barVisibility = true, fabVisibility = true)
                    getMenuItemByTitle(R.string.menu_settings)?.let { setMenuItemIconColor(it) }
                }
                R.id.productInfoFragment -> {
                    showBottomNavigationBar(barVisibility = false, fabVisibility = false)
                }
            }
        }

        // Distribute the menu items evenly
        if (navigationBottomBar.childCount > 0) {
            val actionMenuView = navigationBottomBar.getChildAt(0) as ActionMenuView
            actionMenuView.layoutParams.width = ActionMenuView.LayoutParams.MATCH_PARENT
        }
    }

    private fun showBottomNavigationBar(barVisibility: Boolean, fabVisibility: Boolean) {
        navigationBottomBar.visibility = if (barVisibility) BottomAppBar.VISIBLE else BottomAppBar.GONE
        if (fabVisibility) fab.show() else fab.hide()
    }

    private fun setListeners() {
        fab.setOnClickListener { onAddButtonClick() }

        navigationBottomBar.menu.getItem(0).setOnMenuItemClickListener { onNavigateHomeButtonClick() }
        navigationBottomBar.menu.getItem(1).setOnMenuItemClickListener { onNavigateSearchButtonClick() }
        navigationBottomBar.menu.getItem(4).setOnMenuItemClickListener { onNavigateReportsButtonClick() }
        navigationBottomBar.menu.getItem(5).setOnMenuItemClickListener { onNavigateSettingsButtonClick() }
    }

    private fun setMenuItemIconColor(menuItem: MenuItem) {
        val wrappedDrawable = setDrawableTint(menuItem.icon, R.color.colorPrimary)

        for (x in 0 until navigationBottomBar.menu.size) {
            if (navigationBottomBar.menu.getItem(x).isEnabled && navigationBottomBar.menu.getItem(x).title != menuItem.title) {
                val wrappedDrawableToReset =
                    setDrawableTint(navigationBottomBar.menu.getItem(x).icon, R.color.black)
                navigationBottomBar.menu.getItem(x).icon = wrappedDrawableToReset
            }
        }

        menuItem.icon = wrappedDrawable
    }

    private fun setDrawableTint(drawable: Drawable, color: Int): Drawable? {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(
            wrappedDrawable,
            ContextCompat.getColor(applicationContext, color)
        )
        return wrappedDrawable
    }

    private fun getMenuItemByTitle(title: Int): MenuItem? {
        for (x in 0 until navigationBottomBar.menu.size) {
            if (navigationBottomBar.menu.getItem(x).isEnabled && navigationBottomBar.menu.getItem(x).title == getString(
                    title
                )
            ) {
                return navigationBottomBar.menu.getItem(x)
            }
        }
        return null
    }

    override fun onSupportNavigateUp(): Boolean {
        return when (navController.currentDestination?.id) {
            R.id.scannerFragment, R.id.productInfoFragment -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onSupportNavigateUp()
        }
    }

    override fun onBackPressed() {
        /*
        If current destination is a top level destination, navigate to list. Otherwise follow the
        navigate up flow. If the current destination is the list, close the app.
        */
        val currentFragment = fragmentHost.childFragmentManager.fragments.first()
        if (currentFragment is OnBackPressedListener && currentFragment.handleBackPress()) {
            return
        }
        if (topLevelDestinations.contains(navController.currentDestination?.id)) {
            navController.navigate(R.id.productListFragment)
        } else {
            onSupportNavigateUp()
        }
    }

    private fun onNavigateSettingsButtonClick(): Boolean {
        findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalSettingsFragment())
        return true
    }

    private fun onNavigateSearchButtonClick(): Boolean {
        findNavController(fragmentHost).navigate(
            NavigationGraphDirections.actionGlobalProductListFragment(
                null,
                true
            )
        )
        return true
    }

    private fun onNavigateHomeButtonClick(): Boolean {
        return if (navController.currentDestination?.id != R.id.productListFragment) {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalProductListFragment())
            true
        } else {
            false
        }
    }

    private fun onAddButtonClick() {
        val currentFragment =
            supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments.first()
        val previousFragment: PreviousFragmentIndex = when (currentFragment) {
            is ProductListFragment -> PreviousFragmentIndex.PRODUCT_LIST
            is SettingsFragment -> PreviousFragmentIndex.SETTINGS
            else -> PreviousFragmentIndex.PRODUCT_LIST
        }
        findNavController(fragmentHost).navigate(
            NavigationGraphDirections.actionGlobalScannerFragment(
                previousFragment() as Int
            )
        )
    }

    private fun onNavigateReportsButtonClick(): Boolean {
        return if (navController.currentDestination?.id != R.id.wasteReportFragment) {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalReportsFragment())
            true
        } else {
            false
        }
    }

    interface OnBackPressedListener {

        fun handleBackPress(): Boolean
    }
}
