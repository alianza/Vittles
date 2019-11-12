package com.example.vittles.productlist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_productlist.*
import javax.inject.Inject

/**
 * Fragment class for the main activity. This is the fragment that shows the list of products.
 *
 * @author Arjen Simons
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 * @author Fethi Tewelde
 * @author Marc van Spronsen
 */
class ProductListFragment : DaggerFragment(), ProductListContract.View {

    @Inject
    lateinit var presenter: ProductListPresenter

    private val args: ProductListFragmentArgs by navArgs()

    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var undoSnackbar: Snackbar
    private lateinit var deletedProduct: Product
    private lateinit var deletedProductDeleteType: DeleteType
    private var deletedProductIndex: Int = 0
    private var products = mutableListOf<Product>()
    private var filteredProducts = products
    private val productAdapter = ProductAdapter(products, this::onRemoveButtonClicked)
    private val sortMenu = SortMenu(products, productAdapter)

    private lateinit var rvProducts: RecyclerView
    private lateinit var llSearch: LinearLayout
    private lateinit var tvAddNewVittle: TextView
    private lateinit var tvNoResults: TextView
    private lateinit var svSearch: SearchView
    private lateinit var toolbar: Toolbar
    private lateinit var content: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with(presenter) {
            start(this@ProductListFragment)
        }
        return inflater.inflate(R.layout.fragment_productlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvProducts = view.findViewById(R.id.rvProducts)
        llSearch = view.findViewById(R.id.llSearch)
        tvAddNewVittle = view.findViewById(R.id.tvAddNewVittle)
        tvNoResults = view.findViewById(R.id.tvNoResults)
        svSearch = view.findViewById(R.id.svSearch)
        toolbar = view.findViewById(R.id.toolbar)
        content = view.findViewById(R.id.content)
        onSearchBarClosed()
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        onSearchBarClosed()
        presenter.destroy()
    }

    /**
     * Initializes the RecyclerView and sets EventListeners.
     *
     */
    override fun initViews() {
        setListeners()

        rvProducts.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

        // Set searchView textColor
        val id =
            svSearch.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = svSearch.findViewById(id) as TextView
        textView.setTextColor(Color.BLACK)
        setListeners()

        initUndoSnackbar()

        setItemTouchHelper()

        if (args.withSearch) {
            onSearchBarOpened()
        }
    }

    /**
     * Called when the mainActivity starts.
     * Re-populates the RecyclerView.
     *
     */
    override fun onResume() {
        super.onResume()
        onPopulateRecyclerView()
    }

    /**
     * Sets all necessary event listeners on ui elements
     *
     */
    override fun setListeners() {
        sortLayout.setOnClickListener { onSortMenuOpened() }

        ibtnSearch.setOnClickListener { onSearchBarOpened() }

        svSearch.setOnCloseListener { onSearchBarClosed(); false }

        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText); return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                filter(query); return false
            }

        })

        imgbtnCloseSearch.setOnClickListener { onSearchBarClosed() }
    }


    /**
     * Deletes product and shows a toast to undo the deletion.
     *
     * @param product The product to delete.
     * @param deleteType The deleteType: eaten, thrown_away or removed.
     */
    override fun onSaveDeleteProduct(product: Product, deleteType: DeleteType) {

        if (undoSnackbar.isShown) {
            presenter.deleteProduct(deletedProduct, deletedProductDeleteType)
            //removeItem(deletedProductIndex)
        }
        //set deleted product
        deletedProduct = product
        deletedProductIndex = products.indexOf(product)
        deletedProductDeleteType = deleteType

        products.remove(product)
        //It crashes when you use notifyItemRemoved(0). This has been a known issue for quit a while.
        if (deletedProductIndex == 0) {
            productAdapter.notifyDataSetChanged()
        } else {
            productAdapter.notifyItemRemoved(deletedProductIndex)

            //Makes sure the divider on the element above is drawn
            if(deletedProductIndex != 0) {
                productAdapter.notifyItemChanged(deletedProductIndex - 1)
            }
        }

        onShowUndoSnackbar()
    }

    /**
     * Initializes the undo snackbar.
     *
     */
    override fun initUndoSnackbar(){
        undoSnackbar = Snackbar.make(
            content,
            "",
            Snackbar.LENGTH_SHORT)

        undoSnackbar.setAction("UNDO") {}
        undoSnackbar.setActionTextColor(Color.WHITE)
        undoSnackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)

                if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                    presenter.deleteProduct(deletedProduct, deletedProductDeleteType)
                }
                else{
                    products.add(index = deletedProductIndex, element = deletedProduct)
                    productAdapter.notifyItemInserted(deletedProductIndex)

                    if (deletedProductIndex == products.count() - 1){
                        productAdapter.notifyItemChanged(deletedProductIndex - 1)
                    }
                }
            }
        })
    }

    /**
     * Shows the undo snackbar and sets the text.
     *
     */
    @SuppressLint("DefaultLocale")
    override fun onShowUndoSnackbar(){
        undoSnackbar.setText(deletedProduct.productName + " has been " + deletedProductDeleteType
            .toString()
            .toLowerCase()
            .replace("_", " ")
        )

        undoSnackbar.show()
    }

    /**
     * Handles the action of the remove button on a product
     *
     */
    override fun onRemoveButtonClicked(product: Product) {
        PopupManager.instance.showPopup(context!!,
            PopupBase(
                "Remove Product",
                "Do you want to remove this product? \n It won't be used for the food waste report."
            ),
            PopupButton("NO") {},
            PopupButton("YES") { onSaveDeleteProduct(product, DeleteType.REMOVED) })
    }

    /**
     * Attaches the ItemTouchHelper to the RecyclerView.
     *
     */
    override fun setItemTouchHelper() {
        val callback = ProductItemTouchHelper(products, presenter, context!!, this::onSaveDeleteProduct)
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
        onSearchBarClosed()
    }


    /**
     * Called after filtering products array to show or hide no results textview
     *
     */
    override fun onNoResults() {
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
    override fun onPopulateRecyclerView() {
        products.clear()
        presenter.startPresenting()
    }

    /**
     * When products are loaded, this method will get the products to the product list.
     *
     * @param products Products to be added to the product list.
     */
    override fun onShowProducts(products: List<Product>) {
        this.products.addAll(products)
        presenter.loadIndicationColors(this.products)
        productAdapter.products = products
        productAdapter.notifyDataSetChanged()
        filteredProducts = this.products
        sortMenu.sortFilteredList(filteredProducts)
        setEmptyView()
        onNoResults()
    }

    /**
     * If product could not be deleted, this method will create a feedback Snackbar for the error.
     *
     */
    override fun onShowProductDeleteError() {
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

        onNoResults()
    }

    /**
     * Called when the sort button is clicked.
     *
     */
    override fun onSortMenuOpened() {
        sortMenu.openMenu(context!!, btnSort, filteredProducts)
    }

    /**
     * Method to show the search bar and hide the toolbar
     *
     */
    override fun onSearchBarOpened() {
        llSearch.visibility = View.VISIBLE
        svSearch.isIconified = false

        toolbar.visibility = View.GONE
    }

    /**
     * Method to hide the search bar and show the toolbar
     *
     */
    override fun onSearchBarClosed() {
        svSearch.setQuery("", true)
        llSearch.visibility = View.GONE
        toolbar.visibility = View.VISIBLE
    }
}
