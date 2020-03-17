package com.example.vittles.dashboard.productinfo


import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vittles.NavigationGraphDirections
import com.example.vittles.R
import com.example.vittles.dashboard.model.ProductViewModel
import com.example.vittles.dashboard.productlist.ProductListFragmentDirections
import com.example.vittles.enums.DeleteType
import com.example.vittles.extension.createSnackbar
import com.example.vittles.scanning.ScannerFragment
import com.example.vittles.scanning.productaddmanual.ProductNameEditView
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_product_info.*
import kotlinx.android.synthetic.main.fragment_product_info.layout
import kotlinx.android.synthetic.main.fragment_product_info.tvProductName
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Class for the product info component.
 *
 * @author Arjen Simons
 */
class ProductInfoFragment : DaggerFragment(),
    ProductInfoContract.View {

    /**
     * The presenter of the Fragment.
     */
    @Inject
    lateinit var presenter: ProductInfoPresenter

    /** @suppress */
    private val productArgs: ProductInfoFragmentArgs by navArgs()
    /** @suppress */
    private lateinit var product: ProductViewModel
    /** @suppress */
    private lateinit var updatedProduct: ProductViewModel

    /** {@inheritDoc} */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.start(this@ProductInfoFragment)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_info, container, false)
    }

    /** {@inheritDoc} */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        initViews()
    }

    /**
     * Initializes the views.
     *
     */
    override fun initViews() {
        product = productArgs.product
        updatedProduct = product

        tvCreationDate.text = context!!.resources.getString(
            R.string.expiration_format,
            product.creationDate.dayOfMonth.toString(),
            product.creationDate.monthOfYear.toString(),
            product.creationDate.year.toString()
        )
        updateViews()
        setListeners()
    }

    /**
     * Sets the listeners.
     *
     */
    override fun setListeners() {
        ibEaten.setOnClickListener { onEatenButtonClicked() }
        ibDeleted.setOnClickListener { onDeleteButtonClicked() }
        ibEditName.setOnClickListener { onEditNameClicked() }
        ibEditExpDate.setOnClickListener { onEditExpirationDateClicked() }
    }

    /**
     * Updates the views.
     *
     */
    override fun updateViews() {
        tvProductName.text = this.product.productName
        tvExpirationDate.text = context!!.resources.getString(
            R.string.expiration_format,
            product.expirationDate.dayOfMonth.toString(),
            product.expirationDate.monthOfYear.toString(),
            product.expirationDate.year.toString()
        )
    }

    /**
     * Handles the editName button being clicked.
     *
     */
    override fun onEditNameClicked() {
        val dialog = ProductNameEditView(onFinished = { productName: String, _: Boolean ->
            onNameChanged(productName)
        })
        context?.let { dialog.openDialog(it, tvProductName.text.toString()) }
    }

    /**
     * Handles the edit expiration date button being clicked.
     *
     */
    override fun onEditExpirationDateClicked() {
        val currentDate = DateTime.now()

        val year = currentDate.year
        val month = currentDate.monthOfYear
        val day = currentDate.dayOfMonth
        val dpd = context?.let { it1 ->
            DatePickerDialog(
                it1,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val expirationDate =
                        DateTime(
                            year,
                            monthOfYear + ScannerFragment.MONTHS_OFFSET,
                            dayOfMonth,
                            0,
                            0
                        )
                    onExpirationDateChanged(expirationDate)
                }, year, month - ScannerFragment.MONTHS_OFFSET, day
            )
        }
        if (dpd != null) {
            dpd.setButton(
                DatePickerDialog.BUTTON_NEGATIVE,
                getString(R.string.btn_cancel),
                dpd
            )
            dpd.setButton(
                DatePickerDialog.BUTTON_POSITIVE,
                getString(R.string.btn_confirm),
                dpd
            )
            dpd.datePicker.minDate = currentDate.millis
            dpd.datePicker.updateDate(
                this.product.expirationDate.year,
                this.product.expirationDate.monthOfYear - 1,
                this.product.expirationDate.dayOfMonth
            )
            dpd.show()
        }
    }

    /**
     * Handles the eaten button being clicked.
     *
     */
    override fun onEatenButtonClicked() {
        product.deleteType = DeleteType.EATEN
        NavHostFragment.findNavController(fragmentHost).navigate(
            ProductListFragmentDirections.actionGlobalProductListFragment(
                product
            )
        )
    }

    /**
     * Handles the thrown away button being clicked.
     *
     */
    override fun onDeleteButtonClicked() {
        product.deleteType = DeleteType.THROWN_AWAY
        NavHostFragment.findNavController(fragmentHost).navigate(
            ProductListFragmentDirections.actionGlobalProductListFragment(
                product
            )
        )
    }

    /**
     * Called when the product name is changed.
     *
     */
    override fun onNameChanged(productName: String) {
        updatedProduct = product.apply {
            this.productName = productName
        }
        presenter.onProductUpdate(updatedProduct)
    }

    /**
     * Called when the product expiration date has changed.
     *
     */
    override fun onExpirationDateChanged(expirationDate: DateTime) {
        updatedProduct = product.apply {
            this.expirationDate = expirationDate
        }
        presenter.onProductUpdate(updatedProduct)
    }

    /**
     * Handles the product update success state.
     *
     */
    override fun onProductUpdateSuccess() {
        product = updatedProduct
        updateViews()

        createSnackbar(
            requireContext(),
            layout,
            getString(R.string.product_updated),
            Gravity.CENTER,
            Snackbar.LENGTH_SHORT
        ).show()    }

    /**
     * Handles the product updating fail state.
     *
     */
    override fun onProductUpdateFail() {
        updatedProduct = product

        createSnackbar(
            requireContext(),
            layout,
            getString(R.string.product_updated_failed),
            Gravity.CENTER,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * Handles the back button clicked.
     *
     */
    fun onBackPressed() {
        findNavController().navigate(NavigationGraphDirections.actionGlobalProductListFragment(null))
    }
}
