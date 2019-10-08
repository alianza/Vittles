package com.example.vittles.productlist
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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
 */
class ProductsActivity : BaseActivity() {

    @Inject lateinit var presenter: ProductsPresenter

    private var products = arrayListOf<Product>()
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

    /**
     * Initializes the RecyclerView.
     */
    private fun initViews(){
        fab.setOnClickListener { onAddButtonClick() }

        rvProducts.layoutManager =
            LinearLayoutManager(this@ProductsActivity, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter
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

    @SuppressLint("DefaultLocale")
    private fun filter(query: String) {
        filteredProducts = products.filter {
                product -> product.productName!!.toLowerCase().contains(query.toLowerCase())
        } as MutableList<Product>

        productAdapter.products = filteredProducts
        productAdapter.notifyDataSetChanged()

        showOrHideNoResultsView()
    }


    /**
     * Called after filtering products array to show or hide no results textview
     */
    private fun showOrHideNoResultsView() {
        if (productAdapter.itemCount == 0) {
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
    }

    /**
     * If the products could not be loaded, this method will handle the error.
     *
     */
    fun onProductsLoadFail() {
        println("FAIL")
    }
}
