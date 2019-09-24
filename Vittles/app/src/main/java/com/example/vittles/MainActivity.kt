package com.example.vittles

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var products = arrayListOf<Product>()
    private val productAdapter = ProductAdapter(products)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

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
     * Initializes the RecyclerView
     */
    private fun initViews(){
        rvProducts.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

        populateRecyclerView()
    }

    /**
     * Populates the RecyclerView with items
     */
    private fun populateRecyclerView(){

        products.clear()

        for(i in Product.PRODUCT_NAMES.indices){
            products.add(Product(Product.PRODUCT_NAMES[i], Product.PRODUCT_EXPIRATION_DATES[i], Product.PRODUCTS_DAYS_UNTIL_EXPIRATION[i]))
        }
        productAdapter.notifyDataSetChanged()
    }
}
