package com.example.vittles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.ActionMenuView
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.findNavController as findNavSetup
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.vittles.Reports.ReportsFragmentDirections
import com.example.vittles.productlist.ProductsFragmentDirections
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    fun initViews() {
        initNavigation()
        setListeners()
    }

    fun initNavStyle() {
        navView.elevation = 12f
        navView.cradleVerticalOffset = 10f
        navView.fabCradleMargin = 20f
        navView.fabCradleRoundedCornerRadius = 30f
    }

    fun initNavigation() {
        setSupportActionBar(mainToolbar)
        navController = findNavSetup(R.id.fragment)

        NavigationUI.setupActionBarWithNavController(this, navController)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        mainToolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productsFragment -> showBottomNavigationBar(true)
                R.id.addProductFragment -> showBottomNavigationBar(false)
            }
        }

        if(navView.childCount > 0) {
            val actionMenuView = navView.getChildAt(0) as ActionMenuView
            actionMenuView.layoutParams.width = ActionMenuView.LayoutParams.MATCH_PARENT
        }
    }

    // TODO Fix cradle disappearance
    private fun showBottomNavigationBar(visible: Boolean) {
        when (visible) {
            true -> {
                navView.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }
            false -> {
                navView.visibility = View.GONE
                fab.visibility = View.GONE
            }
        }
    }


    /**
     * Sets all necessary event listeners on ui elements
     *
     */
    fun setListeners() {
        // TODO implement onclicklisteners

        fab.setOnClickListener { onAddButtonClick() }

        navView.menu.getItem(0).setOnMenuItemClickListener { onNavigateHomeButtonClick() }
        navView.menu.getItem(3).setOnMenuItemClickListener { onNavigateReportsButtonClick() }

        navView.setNavigationOnClickListener { menuItem ->
            // Bottom Navigation Drawer menu item clicks
            when (menuItem!!.id) {
                R.id.home -> onNavigateHomeButtonClick()
                R.id.reports -> onNavigateReportsButtonClick()
            }
        }
    }

    private fun onNavigateHomeButtonClick(): Boolean {
        val action = ReportsFragmentDirections.actionReportsFragmentToProductsFragment()
        findNavController(fragment).navigate(action)
        return true
    }

    /**
     * Called when the add button is clicked.
     * It starts the addProduct activity.
     *
     */
    fun onAddButtonClick() {
//        val addProductActivityIntent = Intent(
//            this,
//            AddProductFragment::class.java
//        )
//        startActivity(addProductActivityIntent)
//        closeSearchBar()
        val action = ProductsFragmentDirections.actionProductsFragmentToAddProductActivity2()
        findNavController(fragment).navigate(action)
    }

    fun onNavigateReportsButtonClick(): Boolean {
        // TODO disable back button
        val action = ProductsFragmentDirections.actionProductsFragmentToReportsFragment()
        findNavController(fragment).navigate(action)
        return true
    }


}
