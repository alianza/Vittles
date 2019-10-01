package com.example.vittles

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.vittles.data.AppDatabase
import com.example.vittles.data.ProductDao
import com.example.vittles.model.Product

import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


import kotlinx.android.synthetic.main.content_main.*

/**
 * Activity class for the main activity. This is the activity that shows the list of products.
 *
 * @author Arjen Simons
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
class MainActivity : AppCompatActivity() {
    private lateinit var productDao: ProductDao

    private var products = mutableListOf<Product>()
    private val productAdapter = ProductAdapter(products)

    /**
     * Called when the MainActivity is created.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        productDao = AppDatabase.getDatabase(applicationContext).productDao()
        initViews()
    }

    /**
     * Called when the mainActivity starts.
     * Re-populates the RecyclerView.
     */
    override fun onStart() {
        super.onStart()
        populateRecyclerView()
    }

    /**
     * Called when the option menu is created.
     *.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Handles action bar item clicks.
     *
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Called when the add button is clicked.
     * It starts the addProduct activity.
     */
    private fun onAddButtonClick() {
        val addProductActivityIntent = Intent(
            this,
            AddProductActivity::class.java
        )
        startActivity(addProductActivityIntent)
    }

    /**
     * Initializes the RecyclerView.
     */
    private fun initViews(){

        fab.setOnClickListener { onAddButtonClick() }

        rvProducts.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

        populateRecyclerView()
    }

    /**
     * Populates the RecyclerView with items from the local DataBase.
     */
    private fun populateRecyclerView(){

        products.clear()

        for (i in productDao.getAll()) {
            products.add(i)
        }

        productAdapter.notifyDataSetChanged()
    }
}
