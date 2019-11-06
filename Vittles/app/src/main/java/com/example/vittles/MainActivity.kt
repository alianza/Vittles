package com.example.vittles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.findNavController as findNavSetup
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.vittles.productlist.ProductsFragmentDirections
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

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

    fun initNavigation() {
        setSupportActionBar(mainToolbar)
        val navController = findNavSetup(R.id.fragment)

        NavigationUI.setupActionBarWithNavController(this, navController)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        mainToolbar.setupWithNavController(navController, appBarConfiguration)
    }

    /**
     * Sets all necessary event listeners on ui elements
     *
     */
    fun setListeners() {
        // TODO implement onclicklisteners

        fab.setOnClickListener { onAddButtonClick() }


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


}
