package com.example.vittles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionMenuView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.vittles.productlist.ProductListFragmentDirections
import com.example.vittles.wastereport.WasteReportFragmentDirections

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
        appBarConfiguration.topLevelDestinations.add(R.id.wasteReportFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        mainToolbar.setupWithNavController(navController, appBarConfiguration)

        // Initialize navigation visibility
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productListFragment -> showBottomNavigationBar(
                    barVisibility = true,
                    fabVisibility = true
                )
                R.id.wasteReportFragment -> showBottomNavigationBar(
                    barVisibility = true,
                    fabVisibility = true
                )
                R.id.addProductFragment -> showBottomNavigationBar(
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
        fab.setOnClickListener { onAddButtonClick() }

        // Disable these buttons, since they are placeholders
        navView.menu.getItem(2).isEnabled = false
        navView.menu.getItem(3).isEnabled = false

        navView.menu.getItem(0).setOnMenuItemClickListener { onNavigateHomeButtonClick() }
        navView.menu.getItem(1).setOnMenuItemClickListener { onNavigateSearchButtonClick() }
        navView.menu.getItem(4).setOnMenuItemClickListener { onNavigateReportsButtonClick() }
    }

    /**
     * Navigate to the ProductListFragment with the search bar opened.
     *
     * @return Boolean value that represents if the navigation has succeeded.
     */
    private fun onNavigateSearchButtonClick(): Boolean {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalProductListFragment(true))
            return true
    }

    /**
     * Navigate to the ProductListFragment.
     *
     * @return Boolean value that represents if the navigation has succeeded.
     */
    private fun onNavigateHomeButtonClick(): Boolean {
        return if (navController.currentDestination?.id != R.id.productListFragment) {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalProductListFragment(false))
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
        var action: NavDirections? = null
        when (navController.currentDestination?.id) {
            R.id.productListFragment -> action = ProductListFragmentDirections.actionProductsFragmentToAddProductFragment()
            R.id.wasteReportFragment -> action = WasteReportFragmentDirections.actionReportsFragmentToAddProductFragment()
        }
        if (action != null) {
            findNavController(fragmentHost).navigate(action)
        }
    }

    /**
     * Navigate to the ReportsFragment.
     *
     * @return Boolean value that represents if the navigation has succeeded.
     */
    private fun onNavigateReportsButtonClick(): Boolean {
        return if (navController.currentDestination?.id != R.id.wasteReportFragment) {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalReportsFragment())
            true
        } else {
            false
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.addProductFragment -> navController.navigate(NavigationGraphDirections.actionGlobalProductListFragment())
            else -> navController.navigateUp()
        }
    }
}
