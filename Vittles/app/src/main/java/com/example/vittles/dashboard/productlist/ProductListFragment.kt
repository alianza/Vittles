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
import androidx.core.view.updateMarginsRelative
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vittles.R
import com.example.vittles.enums.DeleteType
import com.example.vittles.dashboard.model.ProductViewModel
import com.example.vittles.dashboard.productlist.ui.list.ProductAdapter
import com.example.vittles.dashboard.productlist.ui.list.ProductItemTouchHelper
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

    private lateinit var itemTouchHelper: ProductItemTouchHelper

    private lateinit var adapter: ProductAdapter

    private var navBarHeight: Int = 0

    private val productArgs: ProductListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_productlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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
                productListToolbar.adapter = it
                productListToolbar.parent = this
            }
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
            setActionTextColor(Color.BLACK)
            setTextColor(Color.BLACK)
            view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_cr)
            val lp = view.layoutParams as CoordinatorLayout.LayoutParams
            lp.updateMarginsRelative(bottom = 40)
            lp.gravity = Gravity.CENTER_HORIZONTAL
            lp.width = (content.width * 0.9).toInt()
            view.layoutParams = lp
        }
    }

    inner class ProductSnackbarCallback(
        private val product: ProductViewModel,
        private val deleteType: DeleteType
    ) : BaseTransientBottomBar.BaseCallback<Snackbar>() {

        private val index = adapter.currentList.indexOf(product)

        init {
            adapter.submitList(adapter.products.apply { remove(product) })
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
                adapter.submitList(adapter.products.apply { add(index, product) })
            } else {
                presenter.onProductDelete(product, deleteType)
            }
            super.onDismissed(transientBottomBar, event)
        }
    }
}
