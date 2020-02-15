package com.example.vittles.productlist

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vittles.R
import com.example.vittles.enums.DeleteType
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_productlist.*
import java.util.*
import javax.inject.Inject

class ProductListFragment : DaggerFragment(), ProductListContract.View {

    @Inject
    lateinit var presenter: ProductListPresenter

    private lateinit var vibrator: Vibrator

    private lateinit var productItemTouchHelper: ProductItemTouchHelper

    private lateinit var productAdapter: ProductAdapter

    private lateinit var undoSnackbar: Snackbar

    private val productArgs: ProductListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_productlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        productItemTouchHelper =
            ProductItemTouchHelper(
                ProductItemTouchHelper.ProductItemTouchCallback(
                    requireContext(),
                    this::onProductSwiped
                )
            )
        productAdapter =
            ProductAdapter(
                this::onProductClicked,
                this::onProductRemoveClicked,
                productItemTouchHelper
            )

        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        with(presenter) { start(this@ProductListFragment) }
        super.onStart()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    private fun initViews() {
        rvProducts.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter.apply { notifyDataSetChanged() }

        productItemTouchHelper.attachToRecyclerView(rvProducts)

        undoSnackbar = Snackbar.make(content, "", Snackbar.LENGTH_LONG)
        undoSnackbar.setAction("UNDO") {}
        undoSnackbar.setActionTextColor(Color.WHITE)
    }

    override fun onProductsUpdated(products: List<ProductViewModel>) {
        productAdapter.products = products.toCollection(arrayListOf())
    }

    private fun onProductClicked(product: ProductViewModel) {

    }

    private fun onProductRemoveClicked(product: ProductViewModel) {
        undoSnackbar
            .addCallback(ProductSnackbarCallback(product, DeleteType.REMOVED))
            .show()
    }

    private fun onProductSwiped(product: ProductViewModel, deleteType: DeleteType) {
        undoSnackbar
            .addCallback(ProductSnackbarCallback(product, deleteType))
            .show()
    }

    inner class ProductSnackbarCallback(
        private val product: ProductViewModel,
        private val deleteType: DeleteType
    ) : BaseTransientBottomBar.BaseCallback<Snackbar>() {

        private val productIndex = productAdapter.products.indexOf(product)

        init {
            productAdapter.run {
                products.removeAt(productIndex)
                notifyItemRemoved(productIndex)
            }
        }

        override fun onShown(transientBottomBar: Snackbar?) {
            transientBottomBar?.setText(
                "${product.productName} has been ${deleteType
                .toString()
                .toLowerCase(Locale.getDefault())
                .replace("_", " ")}"
            )
            super.onShown(transientBottomBar)
        }

        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == Snackbar.Callback.DISMISS_EVENT_MANUAL) {
                presenter.onProductDelete(product, deleteType)
            } else {
                productAdapter.run {
                    products.add(productIndex, product)
                    notifyItemInserted(productIndex)
                }
            }
            super.onDismissed(transientBottomBar, event)
        }
    }
}
