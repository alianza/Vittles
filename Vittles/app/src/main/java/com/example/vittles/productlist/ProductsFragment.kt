package com.example.vittles.productlist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.enums.DeleteType
import com.example.vittles.productadd.AddProductFragment
import com.example.vittles.services.popups.PopupBase
import com.example.vittles.services.popups.PopupButton
import com.example.vittles.services.popups.PopupManager
import com.example.vittles.services.sorting.SortMenu
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_productlist.*
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
class ProductsFragment : DaggerFragment(), ProductsContract.View {

    @Inject
    lateinit var presenter: ProductsPresenter

    private lateinit var itemTouchHelper: ItemTouchHelper
    private var products = mutableListOf<Product>()
    private var filteredProducts = products
    private val productAdapter = ProductAdapter(products, this::onRemoveButtonClicked)
    private val sortMenu = SortMenu(products, productAdapter)

    lateinit var rvProducts: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with(presenter) {
            start(this@ProductsFragment)
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_productlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvProducts = view.findViewById(R.id.rvProducts)
        initViews()
    }


//    /**
//     * Called when the ProductsFragment is created.
//     *
//     */
//    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.AppTheme_NoActionBar)
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
//        with(presenter) {
//            start(this@ProductsFragment)
//        }
//        initViews()
//
//    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }


    /**
     * Initializes the RecyclerView and sets EventListeners.
     *
     */
    override fun initViews() {
        setListeners()
//        supportActionBar?.title = getString(R.string.view_title)

        rvProducts.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

        // TODO searching
        // Set searchView textColor
        val id =
            svSearch.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = svSearch.findViewById(id) as TextView
        textView.setTextColor(Color.BLACK)
        setListeners()

        setItemTouchHelper()
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
    override fun setListeners() {
        // TODO implement onclicklisteners

//        fab.setOnClickListener { onAddButtonClick() }

        sortLayout.setOnClickListener { openSortMenu() }

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
     * Handles the action of the remove button on a product
     *
     */
    private fun onRemoveButtonClicked(product: Product) {
        PopupManager.instance.showPopup(context!!,
            PopupBase(
                "Remove Product",
                "Do you want to remove this product? \n It won't be used for the food waste report."
            ),
            PopupButton("NO") {},
            PopupButton("YES") { presenter.deleteProduct(product, DeleteType.REMOVED) })
    }

    /**
     * Attaches the ItemTouchHelper to the RecyclerView.

     */
    override fun setItemTouchHelper() {
        val callback = ProductItemTouchHelper(products, presenter, context!!)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvProducts)
    }

    /**
     * Checks if emptyView should be visible based on the itemCount.
     *
     */
    override fun setEmptyView() {
        if (productAdapter.itemCount == 0) {
            tvAddNewVittle.visibility = View.VISIBLE
        } else {
            tvAddNewVittle.visibility = View.GONE
        }
    }

    /**
     * Called when the add button is clicked.
     * It starts the addProduct activity.
     *
     */
    override fun onAddButtonClick() {
        val addProductActivityIntent = Intent(
            context,
            AddProductFragment::class.java
        )
        startActivity(addProductActivityIntent)
//        closeSearchBar()
    }


    /**
     * Called after filtering products array to show or hide no results textview
     *
     */
    override fun setNoResultsView() {
        if (productAdapter.itemCount == 0) {
            tvNoResults.visibility = View.VISIBLE
        } else {
            tvNoResults.visibility = View.INVISIBLE
        }
    }


    /**
     * Populates the RecyclerView with items from the local DataBase.
     *
     */
    override fun populateRecyclerView() {
        products.clear()
        presenter.startPresenting()
    }

    /**
     * When products are loaded, this method will get the products to the product list.
     *
     * @param products Products to be added to the product list.
     */
    override fun showProducts(products: List<Product>) {
        this.products.addAll(products)
        presenter.loadIndicationColors(this.products)
        productAdapter.products = products
        productAdapter.notifyDataSetChanged()
        filteredProducts = this.products
        sortMenu.sortFilteredList(filteredProducts)
        setEmptyView()
        setNoResultsView()
    }


    /**
     * If product could not be deleted, this method will create a feedback Snackbar for the error.
     *
     */
    override fun showProductDeleteError() {
        Snackbar.make(rvProducts, getString(R.string.product_deleted_failed), Snackbar.LENGTH_LONG)
            .show()
    }

    /**
     * Method that is called when text is entered in search view
     *
     * @param query entered string used as search query
     */
    @SuppressLint("DefaultLocale")
    override fun filter(query: String) {
        filteredProducts = products.filter { product ->
            product.productName.toLowerCase().contains(query.toLowerCase())
        } as MutableList<Product>

        productAdapter.products = filteredProducts
        productAdapter.notifyDataSetChanged()
        sortMenu.sortFilteredList(filteredProducts)

        setNoResultsView()
    }

    /**
     * Called when the sort button is clicked.
     *
     */
    // TODO sorting
    private fun openSortMenu() {
        sortMenu.openMenu(context!!, btnSort, filteredProducts)
    }

    // TODO searching

    /**
     * Method to show the search bar and hide the toolbar
     *
     */
    fun openSearchBar() {
        llSearch.visibility = View.VISIBLE
        svSearch.isIconified = false

        toolbar.visibility = View.GONE
    }

    /**
     * Method to hide the search bar and show the toolbar
     *
     */
    fun closeSearchBar() {
        llSearch.visibility = View.GONE
        svSearch.setQuery("", true)
        toolbar.visibility = View.VISIBLE
    }
}
