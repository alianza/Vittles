package com.example.vittles.productlist.productlist

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vittles.R
import com.example.vittles.enums.DeleteType
import com.example.vittles.productlist.model.ProductViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_productlist.*
import java.util.*
import javax.inject.Inject

class ProductListFragment : DaggerFragment(), ProductListContract.View {

    @Inject
    lateinit var presenter: ProductListPresenter

    private lateinit var vibrator: Vibrator

    private lateinit var itemTouchHelper: ProductItemTouchHelper

    private lateinit var adapter: ProductAdapter

    private val productArgs: ProductListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_productlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        itemTouchHelper =
            ProductItemTouchHelper(
                ProductItemTouchHelper.ProductItemTouchCallback(
                    requireContext(),
                    this::onProductSwiped
                )
            )
        adapter =
            ProductAdapter(
                this::onProductClicked,
                this::onProductRemoveClicked,
                itemTouchHelper
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
        rvProducts.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun supportsPredictiveItemAnimations(): Boolean = true
        }.apply {
            supportsPredictiveItemAnimations()
        }
        rvProducts.adapter = adapter

        itemTouchHelper.attachToRecyclerView(rvProducts)
    }

    override fun onProductsUpdated(products: List<ProductViewModel>) {
        adapter.submitList(products)
    }

    private fun onProductClicked(product: ProductViewModel) {

    }

    private fun onProductRemoveClicked(product: ProductViewModel) {
        createSnackbar()
            .addCallback(ProductSnackbarCallback(product, DeleteType.REMOVED))
            .show()
    }

    private fun onProductSwiped(product: ProductViewModel, deleteType: DeleteType) {
        createSnackbar()
            .addCallback(ProductSnackbarCallback(product, deleteType))
            .show()
    }

    private fun createSnackbar(): Snackbar {
        return Snackbar.make(content, "", Snackbar.LENGTH_LONG).apply {
            setAction("UNDO") {}
            setActionTextColor(Color.WHITE)
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
            transientBottomBar?.setText(
                "${product.productName} has been ${deleteType
                .toString()
                .toLowerCase(Locale.getDefault())
                .replace("_", " ")}"
            )
            super.onShown(transientBottomBar)
        }

        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                presenter.onProductInsert(product)
            }
            super.onDismissed(transientBottomBar, event)
        }
    }
}
