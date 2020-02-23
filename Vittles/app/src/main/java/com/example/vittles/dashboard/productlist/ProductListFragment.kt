@file:Suppress("DEPRECATION")

package com.example.vittles.dashboard.productlist

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.product.model.ProductSortingType
import com.example.vittles.R
import com.example.vittles.dashboard.model.ProductViewModel
import com.example.vittles.dashboard.productlist.ui.list.ProductAdapter
import com.example.vittles.dashboard.productlist.ui.list.ProductItemTouchHelper
import com.example.vittles.dashboard.productlist.ui.toolbar.ProductListToolbar
import com.example.vittles.enums.DeleteType
import com.example.vittles.services.popups.PopupBase
import com.example.vittles.services.popups.PopupButton
import com.example.vittles.services.popups.PopupManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_productlist.*
import java.util.*
import javax.inject.Inject

class ProductListFragment : DaggerFragment(), ProductListContract.View, ProductListToolbar.ProductListToolbarListener {

    @Inject
    lateinit var presenter: ProductListPresenter

    private lateinit var vibrator: Vibrator

    private lateinit var itemTouchHelper: ProductItemTouchHelper

    private lateinit var adapter: ProductAdapter

    private var navBarHeight: Int = 0

    private val productArgs: ProductListFragmentArgs by navArgs()
    private var deleteProductOnInitialize = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with(presenter) { start(this@ProductListFragment) }
        return inflater.inflate(R.layout.fragment_productlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        deleteProductOnInitialize = productArgs.productToDelete != null
        initViews()
        presenter.onListInitializeOrChange(productListToolbar.sortingType)
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    private fun initViews() {
        rvProducts.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun supportsPredictiveItemAnimations(): Boolean = true
        }.apply {
            supportsPredictiveItemAnimations()
        }

        val unbounded = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        navBarHeight = requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).run {
            measure(unbounded, unbounded)
            measuredHeight
        }
        rvProducts.setPadding(0, 0, 0, navBarHeight)

        itemTouchHelper =
            ProductItemTouchHelper(
                ProductItemTouchHelper.ProductItemTouchCallback(
                    requireContext(),
                    this::onProductSwiped
                )
            ).apply {
                attachToRecyclerView(rvProducts)
            }

        adapter =
            ProductAdapter(
                this::onProductClicked,
                this::onProductRemoveClicked,
                itemTouchHelper
            ).also {
                rvProducts.adapter = it
                productListToolbar.parent = this
            }

        productListToolbar.productListToolbarListener = this

        if (productArgs.withSearch) {
            productListToolbar.openSearchInput()
        }
    }

    override fun onProductsUpdated(products: List<ProductViewModel>) {
        adapter.submitList(products) {
            if (deleteProductOnInitialize) {
                deleteProductOnInitialize = false
                productArgs.productToDelete?.let { product ->
                    productArgs.productToDelete?.deleteType?.let { onProductSwiped(product, it) }
                }
            }
        }
    }

    private fun onProductClicked(product: ProductViewModel) {
        NavHostFragment.findNavController(fragmentHost)
            .navigate(ProductListFragmentDirections.actionProductListFragmentToProductInfoFragment(product))
    }

    private fun onProductRemoveClicked(product: ProductViewModel) {
        /* Show pop-up for extra confirmation */
        PopupManager.instance.showPopup(context!!,
            PopupBase(
                getString(R.string.remove_product_header),
                getString(R.string.remove_product_subText)
            ),
            PopupButton(getString(R.string.btn_no).toUpperCase(Locale.getDefault())),
            PopupButton(getString(R.string.btn_yes).toUpperCase(Locale.getDefault())) {
                createSnackbar()
                    .addCallback(ProductSnackbarCallback(product, DeleteType.REMOVED))
                    .show()
            })
    }

    private fun onProductSwiped(product: ProductViewModel, deleteType: DeleteType) {
        if (vibrator.hasVibrator() && presenter.getVibrationSetting()) {
            vibrator.vibrate(50)
        }
        createSnackbar()
            .addCallback(ProductSnackbarCallback(product, deleteType))
            .show()
    }

    override fun onSortingTypeChanged(sortingType: ProductSortingType) {
        presenter.onListInitializeOrChange(sortingType)
    }

    override fun onQueryTextChanged(query: String?) {
        query?.let { presenter.onListInitializeOrChange(productListToolbar.sortingType, it) }
    }

    private fun createSnackbar(): Snackbar {
        return Snackbar.make(content, "", Snackbar.LENGTH_LONG).apply {
            setAction("UNDO") {}
            setActionTextColor(Color.BLACK)
            setTextColor(Color.BLACK)
            view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_cr)
            val lp = view.layoutParams as CoordinatorLayout.LayoutParams
            lp.gravity = Gravity.CENTER_HORIZONTAL
            lp.width = (content.width * 0.9).toInt()
            view.layoutParams = lp
        }
    }

    inner class ProductSnackbarCallback(
        private val product: ProductViewModel,
        private val deleteType: DeleteType
    ) : BaseTransientBottomBar.BaseCallback<Snackbar>() {

        init {
            presenter.onProductDelete(product, deleteType)
        }

        override fun onShown(transientBottomBar: Snackbar?) {
            super.onShown(transientBottomBar)
            transientBottomBar?.setText(
                "${product.productName} has been ${deleteType
                    .toString()
                    .toLowerCase(Locale.getDefault())
                    .replace("_", " ")}"
            )
        }

        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                presenter.onProductInsert(product)
            } else {
                presenter.onProductDelete(product, deleteType)
            }
            super.onDismissed(transientBottomBar, event)
        }
    }
}
