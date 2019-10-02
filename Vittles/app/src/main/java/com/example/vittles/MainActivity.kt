package com.example.vittles

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.example.vittles.data.AppDatabase
import com.example.vittles.data.ProductDao
import com.example.vittles.model.Product
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
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
    private var filteredProducts = products
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
     * Initializes the RecyclerView and sets EventListeners.
     */
    private fun initViews(){

        setListeners()

        rvProducts.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

        // Set searchView textColor
        val id =
            searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = searchView.findViewById(id) as TextView
        textView.setTextColor(Color.BLACK)

        populateRecyclerView()
    }


    /**
     * Called when the mainActivity starts.
     * Re-populates the RecyclerView.
     */
    override fun onStart() {
        super.onStart()
        populateRecyclerView()
    }

    private fun setListeners() {

        fab.setOnClickListener { onAddButtonClick() }

        ibtnSearch.setOnClickListener { openSearchBar() }

        searchView.setOnCloseListener { closeSearchBar(); false }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean { filter(newText); return false }

            override fun onQueryTextSubmit(query: String): Boolean { filter(query); return false }

        })

        imgbtnCloseSearch.setOnClickListener { closeSearchBar() }
    }

//    /**
//     * Called when the option menu is created.
//     *.
//     */
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    /**
//     * Handles action bar item clicks.
//     *
//     */
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

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

    @SuppressLint("DefaultLocale")
    private fun filter(query: String) {
        filteredProducts = products.filter {
                product -> product.productName!!.toLowerCase().contains(query.toLowerCase())
        } as MutableList<Product>

        println("filtered: $filteredProducts")
        productAdapter.products = filteredProducts
        productAdapter.notifyDataSetChanged()

        if (filteredProducts.size == 0) {
            tvNoResults.visibility = View.VISIBLE
        } else {
            tvNoResults.visibility = View.INVISIBLE
        }
    }

    private fun openSearchBar() {
        llSearch.visibility = View.VISIBLE
        searchView.isIconified = false

        toolbar.visibility = View.GONE
    }

    private fun closeSearchBar() {
        llSearch.visibility = View.GONE
        toolbar.visibility = View.VISIBLE
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
