package com.example.vittles.productlist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Product
import com.example.vittles.R
import com.example.vittles.VittlesApp
import com.example.vittles.mvp.BaseActivity
import com.example.vittles.productadd.AddProductActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

/**
 * Activity class for the main activity. This is the activity that shows the list of products.
 *
 * @author Arjen Simons
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 * @author Fethi Tewelde
 */
class ProductsActivity : BaseActivity() {

    @Inject
    lateinit var presenter: ProductsPresenter

    private var products = mutableListOf<Product>()
    private var filteredProducts = products
    private val productAdapter = ProductAdapter(products)

    /**
     * Called when the ProductsActivity is created.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.inject
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        with(presenter) {
            start(this@ProductsActivity)
        }
        initViews()
    }

    override fun injectDependencies() {
        DaggerProductsComponent.builder()
            .appComponent(VittlesApp.component)
            .productsModule(ProductsModule())
            .build()
            .inject(this)
    }


    /**
     * Initializes the RecyclerView and sets EventListeners.
     *
     */
    private fun initViews() {
        setListeners()

        rvProducts.layoutManager =
            LinearLayoutManager(this@ProductsActivity, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

        // Set searchView textColor
        val id =
            searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = searchView.findViewById(id) as TextView
        textView.setTextColor(Color.BLACK)
    }


    /**
     * Called when the mainActivity starts.
     * Re-populates the RecyclerView.
     *
     */
    override fun onResume() {
        super.onResume()
        populateRecyclerView()
    }

    /**
     * Sets all necessary event listeners on ui elements
     *
     */
    private fun setListeners() {

        fab.setOnClickListener { onAddButtonClick() }

        ibtnSearch.setOnClickListener { openSearchBar() }

        searchView.setOnCloseListener { closeSearchBar(); false }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText); return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                filter(query); return false
            }

        })

        imgbtnCloseSearch.setOnClickListener { closeSearchBar() }
    }

    /**
     * Checks if emptyView should be visible based on the itemCount
     */
    private fun setEmptyView() {
        if (productAdapter.itemCount == 0) {
            tvEmptyView.visibility = View.VISIBLE
        } else {
            tvEmptyView.visibility = View.GONE
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
     * Method that is called when text is entered in search view
     *
     * @param query entered string used as search query
     */
    @SuppressLint("DefaultLocale")
    private fun filter(query: String) {
        filteredProducts = products.filter { product ->
            product.productName!!.toLowerCase().contains(query.toLowerCase())
        } as MutableList<Product>

        productAdapter.products = filteredProducts
        productAdapter.notifyDataSetChanged()

        setNoResultsView()
    }


    /**
     * Called after filtering products array to show or hide no results textview
     *
     */
    private fun setNoResultsView() {
        if (productAdapter.itemCount == 0) {
            tvNoResults.visibility = View.VISIBLE
        } else {
            tvNoResults.visibility = View.INVISIBLE
        }
    }

    /**
     * Method to show the search bar and hide the toolbar
     *
     */
    private fun openSearchBar() {
        llSearch.visibility = View.VISIBLE
        searchView.isIconified = false

        toolbar.visibility = View.GONE
    }

    /**
     * Method to hide the search bar and show the toolbar
     *
     */
    private fun closeSearchBar() {
        llSearch.visibility = View.GONE
        toolbar.visibility = View.VISIBLE
    }

    /**
     * Populates the RecyclerView with items from the local DataBase.
     *
     */
    private fun populateRecyclerView() {
        products.clear()
        presenter.loadProducts()
    }

    /**
     * When products are loaded, this method will add the products to the product list.
     *
     * @param products Products to be added to the product list.
     */
    fun onProductsLoadSucceed(products: List<Product>) {
        this.products.addAll(products)
        presenter.loadIndicationColors(this.products)
        productAdapter.notifyDataSetChanged()
        setEmptyView()
    }

    /**
     * If the products could not be loaded, this method will handle the error.
     *
     */
    fun onProductsLoadFail() {
        println("FAIL")
    }
}
