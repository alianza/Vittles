package com.example.vittles.productlist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
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
import kotlinx.android.synthetic.main.activity_sort.*
import kotlinx.android.synthetic.main.activity_sort.view.*
import kotlinx.android.synthetic.main.activity_sort.view.daysRemainingLH
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

/**
 * Activity class for the main activity. This is the activity that shows the list of products.
 *
 * @author Arjen Simons
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 * @author Fethi Tewelde
 * @author Marc van Spronsen
 */
class ProductsActivity : BaseActivity() {

    @Inject
    lateinit var presenter: ProductsPresenter

    private var products = mutableListOf<Product>()
    private var filteredProducts = products
    private val productAdapter = ProductAdapter(products)
    private var selectedSortNumber = 1
    lateinit var selectedSortMethod: ImageView

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
        supportActionBar?.title = getString(R.string.view_title)

        rvProducts.layoutManager =
            LinearLayoutManager(this@ProductsActivity, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

        // Set searchView textColor
        val id =
            svSearch.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = svSearch.findViewById(id) as TextView
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

        btnSort.setOnClickListener { openSortMenu() }

        ibtnSearch.setOnClickListener { openSearchBar() }

        svSearch.setOnCloseListener { closeSearchBar(); false }

        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

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
     * Called when the sort button is clicked.
     * It opens the sorting menu.
     * It handels all click events of the dialog window
     * It sort the products list
     */
    private fun openSortMenu() {

        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.activity_sort, null)
        val mBuilder =
            AlertDialog.Builder(this).setView(mDialogView)
        val  mAlertDialog = mBuilder.show()

        if (!::selectedSortMethod.isInitialized) {
            selectedSortMethod = mDialogView.daysRemainingAsc
            setSortMenuColor(selectedSortMethod)
        }
        else {
            selectedSortMethod = when {
                selectedSortNumber == 1 -> mDialogView.daysRemainingAsc
                selectedSortNumber == 2 -> mDialogView.daysRemainingDesc
                selectedSortNumber == 3 -> mDialogView.alfabeticAz
                selectedSortNumber == 4 -> mDialogView.alfabeticZa
                selectedSortNumber == 5 -> mDialogView.newestSelected
                else -> mDialogView.oldestSelected
            }
            setSortMenuColor(selectedSortMethod)
        }

        mDialogView.daysRemainingLH.setOnClickListener {
            products.sortBy { it.expirationDate }
            btnSort.text = mDialogView.daysRemainingLH.text
            selectedSortNumber = 1
            productAdapter.notifyDataSetChanged()
            mAlertDialog.dismiss()
        }
        mDialogView.daysRemainingHL.setOnClickListener {
            products.sortByDescending { it.expirationDate }
            btnSort.text = mDialogView.daysRemainingHL.text
            selectedSortNumber = 2
            productAdapter.notifyDataSetChanged()
            mAlertDialog.dismiss()
        }
        mDialogView.alfabeticAZ.setOnClickListener {
            products.sortBy { it.productName }
            btnSort.text = mDialogView.alfabeticAZ.text
            selectedSortNumber = 3
            productAdapter.notifyDataSetChanged()
            mAlertDialog.dismiss()
        }
        mDialogView.alfabeticZA.setOnClickListener {
            products.sortByDescending { it.productName }
            btnSort.text = mDialogView.alfabeticZA.text
            selectedSortNumber = 4
            productAdapter.notifyDataSetChanged()
            mAlertDialog.dismiss()
        }
        mDialogView.newest.setOnClickListener {
            products.sortByDescending { it.creationDate }
            btnSort.text = mDialogView.newest.text
            selectedSortNumber = 5
            productAdapter.notifyDataSetChanged()
            mAlertDialog.dismiss()
        }
        mDialogView.oldest.setOnClickListener {
            products.sortBy { it.creationDate }
            btnSort.text = mDialogView.oldest.text
            selectedSortNumber = 6
            productAdapter.notifyDataSetChanged()
            mAlertDialog.dismiss()
        }
    }

    /**
     * Sets the circle alpha to 1 of the selected sorting method.
     *
     * @param circle The Imageview that represents the selected sorting method.
     */
    fun setSortMenuColor(circle: ImageView) {
        circle.alpha = 1f
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
        closeSearchBar()
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
        svSearch.isIconified = false

        toolbar.visibility = View.GONE
    }

    /**
     * Method to hide the search bar and show the toolbar
     *
     */
    private fun closeSearchBar() {
        llSearch.visibility = View.GONE
        svSearch.setQuery("", true)
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
