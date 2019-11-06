package com.example.vittles.productlist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.enums.DeleteType
import com.example.vittles.productadd.AddProductActivity
import com.example.vittles.services.popups.PopupBase
import com.example.vittles.services.popups.PopupButton
import com.example.vittles.services.popups.PopupManager
import com.example.vittles.services.sorting.SortMenu
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
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
class ProductsActivity : DaggerAppCompatActivity(), ProductsContract.View {

    @Inject
    lateinit var presenter: ProductsPresenter

    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var undoSnackbar: Snackbar
    private var deletedProduct: Deque<Product> = ArrayDeque()
    private var deletedProductDeleteType: Deque<DeleteType> = ArrayDeque()
    private var deletedProductIndex: Deque<Int> = ArrayDeque()
    private var products = mutableListOf<Product>()
    private var filteredProducts = products
    private val productAdapter = ProductAdapter(products, this::onRemoveButtonClicked)
    private val sortMenu = SortMenu(products, productAdapter)

    /**
     * Called when the ProductsActivity is created.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        with(presenter) {
            start(this@ProductsActivity)
        }
        initViews()
    }

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
        supportActionBar?.title = getString(R.string.view_title)

        rvProducts.layoutManager =
            LinearLayoutManager(this@ProductsActivity, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

        // Set searchView textColor
        val id =
            svSearch.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = svSearch.findViewById(id) as TextView
        textView.setTextColor(Color.BLACK)

        initUndoSnackbar()

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
     * Deletes product and shows a toast to undo the deletion.
     *
     * @param product The product to delete.
     * @param deleteType The deleteType: eaten, thrown_away or removed.
     */
    private fun saveDeleteProduct(product: Product, deleteType: DeleteType) {

        //set deleted product
        deletedProduct.push(product)
        deletedProductIndex.push(products.indexOf(product))
        deletedProductDeleteType.push(deleteType)

        products.remove(product)
        //It crashes when you use notifyItemRemoved(0). This has been a known issue for quit a while.
        if (deletedProductIndex.peekFirst() == 0) {
            productAdapter.notifyDataSetChanged()
        } else {
            productAdapter.notifyItemRemoved(deletedProductIndex.first)
            //productAdapter.notifyDataSetChanged()
        }

        showUndoSnackbar()
    }

    /**
     * Initializes the undo snackbar.
     *
     */
    private fun initUndoSnackbar(){
        undoSnackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "",
            Snackbar.LENGTH_SHORT)

        undoSnackbar.setAction("UNDO") {}
        undoSnackbar.setActionTextColor(Color.WHITE)
        undoSnackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)

                if(event != Snackbar.Callback.DISMISS_EVENT_ACTION){
                    presenter.deleteProduct(deletedProduct.removeLast(), deletedProductDeleteType.removeLast())
                    deletedProductIndex.removeLast()
                }
                else{
                    products.add(index = deletedProductIndex.first, element = deletedProduct.removeFirst())
                    productAdapter.notifyItemInserted(deletedProductIndex.removeFirst())
                    //productAdapter.notifyDataSetChanged()

                    deletedProductDeleteType.removeLast()
                }
            }
        })
    }

    /**
     * Shows the undo snackbar and sets the text.
     *
     */
    private fun showUndoSnackbar(){
        if (undoSnackbar.isShown) { undoSnackbar.dismiss() }

//        undoSnackbar = Snackbar.make(
//            findViewById(android.R.id.content),
//            deletedProduct.productName + " has been " + deleteType
//                .toString()
//                .toLowerCase()
//                .replace("_", " "),
//            Snackbar.LENGTH_SHORT
//        )

        undoSnackbar.setText(deletedProduct.first.productName + " has been " + deletedProductDeleteType.first
            .toString()
            .toLowerCase()
            .replace("_", " ")
        )

        undoSnackbar.show()
    }
    
     /**
     * Called when the sort button is clicked.
     *
     */
    private fun openSortMenu() {
        sortMenu.openMenu(this, btnSort, filteredProducts)
    }

    /**
     * Handles the action of the remove button on a product
     *
     */
    private fun onRemoveButtonClicked(product: Product){
        PopupManager.instance.showPopup(this,
            PopupBase("Remove Product", "Do you want to remove this product? \n It won't be used for the food waste report."),
            PopupButton("NO") {},
            PopupButton("YES") { saveDeleteProduct(product, DeleteType.REMOVED) })
    }

    /**
     * Attaches the ItemTouchHelper to the RecyclerView.
    
     */
    override fun setItemTouchHelper() {
        val callback = ProductItemTouchHelper(products,presenter,this, this::saveDeleteProduct)
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
     * Method to show the search bar and hide the toolbar
     *
     */
    override fun openSearchBar() {
        llSearch.visibility = View.VISIBLE
        svSearch.isIconified = false

        toolbar.visibility = View.GONE
    }

    /**
     * Method to hide the search bar and show the toolbar
     *
     */
    override fun closeSearchBar() {
        llSearch.visibility = View.GONE
        svSearch.setQuery("", true)
        toolbar.visibility = View.VISIBLE
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
}
