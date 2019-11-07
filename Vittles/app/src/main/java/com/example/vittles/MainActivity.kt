package com.example.vittles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionMenuView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.vittles.productlist.ProductsFragmentDirections
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.content_main.*
import androidx.navigation.findNavController as findNavSetup

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        initViews()
    }

    fun initViews() {
        initNavigation()
        setListeners()
    }

    fun initNavigation() {
        navController = findNavSetup(R.id.fragmentHost)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        appBarConfiguration.topLevelDestinations.add(R.id.reportsFragment)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)


        mainToolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productsFragment -> showBottomNavigationBar(
                    barVisibility = true,
                    fabVisibility = true
                )
                R.id.reportsFragment -> showBottomNavigationBar(
                    barVisibility = true,
                    fabVisibility = true
                )
                R.id.addProductFragment -> showBottomNavigationBar(
                    barVisibility = false,
                    fabVisibility = false
                )
            }
        }

        if(navView.childCount > 0) {
            val actionMenuView = navView.getChildAt(0) as ActionMenuView
            actionMenuView.layoutParams.width = ActionMenuView.LayoutParams.MATCH_PARENT
        }
    }

    private fun showBottomNavigationBar(barVisibility: Boolean, fabVisibility: Boolean) {
        navView.visibility =  if(barVisibility)  BottomAppBar.VISIBLE else BottomAppBar.GONE
        if (fabVisibility) fab.show() else fab.hide()
    }


    /**
     * Sets all necessary event listeners on ui elements
     *
     */
    fun setListeners() {
        fab.setOnClickListener { onAddButtonClick() }

        navView.menu.getItem(2).isEnabled = false
        navView.menu.getItem(3).isEnabled = false

        navView.menu.getItem(0).setOnMenuItemClickListener { onNavigateHomeButtonClick() }
        navView.menu.getItem(4).setOnMenuItemClickListener { onNavigateReportsButtonClick() }
    }

    private fun onNavigateHomeButtonClick(): Boolean {
        return if (navController.currentDestination?.id != R.id.productsFragment) {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalProductsFragment())
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
    fun onAddButtonClick() {
        val action = ProductsFragmentDirections.actionProductsFragmentToAddProductFragment()
        findNavController(fragmentHost).navigate(action)
    }

    fun onNavigateReportsButtonClick(): Boolean {
        return if (navController.currentDestination?.id != R.id.reportsFragment) {
            findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalReportsFragment())
            true
        } else {
            false
        }
    }


}
