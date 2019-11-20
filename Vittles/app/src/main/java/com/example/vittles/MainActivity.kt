package com.example.vittles

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionMenuView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.size
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.vittles.productlist.ProductListFragment
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.content_main.*
import androidx.navigation.findNavController as findNavSetup

/**
 * Main activity that only controls the navigation.
 *
 * @author Jeroen Flietstra
 * @author Fethi Tewelde
 */
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar) // Finish splash screen
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        initViews()
    }

    /**
     * Initializes the view elements.
     *
     */
    private fun initViews() {
        initNavigation()
        setListeners()
    }

    /**
     * Initializes the bottom navigation.
     *
     */
    private fun initNavigation() {
        navController = findNavSetup(R.id.fragmentHost)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        // Make reports top-level so that the back button disables
        appBarConfiguration.topLevelDestinations.add(R.id.reportsFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        mainToolbar.setupWithNavController(navController, appBarConfiguration)

        setDrawableTint(getMenuItemByTitle(R.string.menu_home)!!.icon, R.color.colorPrimary)

        // Initialize navigation visibility
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productListFragment -> showBottomNavigationBar(
                    barVisibility = true,
                    fabVisibility = true
                )
                R.id.reportsFragment -> showBottomNavigationBar(
                    barVisibility = true,
                    fabVisibility = true
                )
                R.id.scannerFragment -> showBottomNavigationBar(
                    barVisibility = false,
                    fabVisibility = false
                )
            }
        }

        // Distribute the menu items evenly
        if(navView.childCount > 0) {
            val actionMenuView = navView.getChildAt(0) as ActionMenuView
            actionMenuView.layoutParams.width = ActionMenuView.LayoutParams.MATCH_PARENT
        }
    }

    /**
     * Will hide/show the navigation bar and/or the floating action button.
     *
     * @param barVisibility Boolean value that represents if the navigation should be visible.
     * @param fabVisibility Boolean value that represents if the FAB should be visible.
     */
    private fun showBottomNavigationBar(barVisibility: Boolean, fabVisibility: Boolean) {
        navView.visibility =  if(barVisibility)  BottomAppBar.VISIBLE else BottomAppBar.GONE
        if (fabVisibility) fab.show() else fab.hide()
    }

    /**
     * Sets all necessary event listeners on UI elements.
     *
     */
    private fun setListeners() {
        fab.setOnClickListener { setMenuIconColor(0); onAddButtonClick() }

        navView.menu.getItem(0).setOnMenuItemClickListener { setMenuIconColor(0); onNavigateHomeButtonClick() }
        navView.menu.getItem(1).setOnMenuItemClickListener { setMenuIconColor(1); onNavigateSearchButtonClick()}
        navView.menu.getItem(4).setOnMenuItemClickListener { setMenuIconColor(4); onNavigateReportsButtonClick() }
        navView.menu.getItem(5).setOnMenuItemClickListener { setMenuIconColor(5); onNavigateSettingsButtonClick() }
    }

    /**
     * Sets the color of menu icon that is pressed
     *
     * @param index Index of pressed menu item
     */
    private fun setMenuIconColor(index: Int) {
        val wrappedDrawable = setDrawableTint(navView.menu.getItem(index).icon, R.color.colorPrimary)

        for (x in 0 until navView.menu.size) {
            if (navView.menu.getItem(x).isEnabled && x != index) {
                val wrappedDrawableToReset = setDrawableTint(navView.menu.getItem(x).icon, R.color.black)
                navView.menu.getItem(x).icon = wrappedDrawableToReset
            }
        }

        navView.menu.getItem(index).icon = wrappedDrawable

        searchIconException(index)
    }

    /**
     * Handles the exception of the search icon (Home icon is highlighted)
     *
     * @param index Index of potential search icon menu item
     */
    private fun searchIconException(index: Int) {
        if (navView.menu.getItem(index).title === getString(R.string.menu_search)) {
            val wrappedDrawable = setDrawableTint(navView.menu.getItem(index).icon, R.color.black)
            navView.menu.getItem(index).icon = wrappedDrawable
            setDrawableTint(getMenuItemByTitle(R.string.menu_home)!!.icon, R.color.colorPrimary)
        }
    }

    /**
     * Sets the tint of a drawable and returns the drawable
     *
     * @param drawable Drawable to modify tint of
     * @param color Color to modify tint of drawable to
     * @return Returns modified drawable
     */
    private fun setDrawableTint(drawable: Drawable, color: Int): Drawable? {
        val wrappedDrawable = DrawableCompat.wrap(drawable)

        DrawableCompat.setTint(
            wrappedDrawable,
            ContextCompat.getColor(applicationContext, color)
        )
        return wrappedDrawable
    }

    /**
     * Gets a menu item based on it's title
     *
     * @param title title to search MenuItem on
     * @return returns found MenuItem of null if not found
     */
    private fun getMenuItemByTitle(title: Int): MenuItem? {
        for (x in 0 until navView.menu.size) {
            if (navView.menu.getItem(x).isEnabled && navView.menu.getItem(x).title == getString(title)) {
                return navView.menu.getItem(x)
            }
        }
        return null
    }

    /**
     * Navigate to the SettingsFragment with the search bar opened.
     *
     * @return Boolean value that represents if the navigation has succeeded.
     */
    private fun onNavigateSettingsButtonClick(): Boolean {
        // TODO Implement settings button click
        println("onNavigateSettingsButtonClick TODO")
        return true
    }

    /**
     * Navigate to the ProductListFragment with the search bar opened.
     *
     * @return Boolean value that represents if the navigation has succeeded.
     */
    private fun onNavigateSearchButtonClick(): Boolean {
            ProductListFragment.withSearch = true
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalProductListFragment())
            return true
    }

    /**
     * Navigate to the ProductListFragment.
     *
     * @return Boolean value that represents if the navigation has succeeded.
     */
    private fun onNavigateHomeButtonClick(): Boolean {
        return if (navController.currentDestination?.id != R.id.productListFragment) {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalProductListFragment())
            true
        } else {
            false
        }
    }

    /**
     * Called when the add button is clicked.
     * It starts the addProduct activity.
     *
     */
    private fun onAddButtonClick() {
        findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalScannerFragment())
    }

    /**
     * Navigate to the ReportsFragment.
     *
     * @return Boolean value that represents if the navigation has succeeded.
     */
    private fun onNavigateReportsButtonClick(): Boolean {
        return if (navController.currentDestination?.id != R.id.reportsFragment) {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalReportsFragment())
            true
        } else {
            false
        }
    }
}
