package com.example.vittles.productinfo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.domain.product.Product
import com.example.vittles.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Class for the product info component
 *
 */
class ProductInfoFragment : DaggerFragment(), ProductInfoContract.View {

    val productArgs: ProductInfoFragmentArgs by navArgs()

    /**
     * The presenter of the Fragment.
     */
    @Inject
    lateinit var presenter: ProductInfoPresenter

    /** @suppress */
    private lateinit var product: Product
    private lateinit var updatedProduct: Product

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

        initViews()
    }

    /**
     * Initializes the views.
     *
     */
    override fun initViews() {
        product = productArgs.product
        updatedProduct = product

//        val intent = activity!!.intent
//        product = intent.getParcelableExtra(getString(R.string.product))
//

        updateViews()
    }

    /**
     * Updates the views
     *
     */
    override fun updateViews() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Called when the product name is changed.
     *
     */
    override fun onNameChanged() {
        //updatedProduct.name = input
        presenter.updateProduct(updatedProduct)

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Called when the product expiration date has changed.
     *
     */
    override fun onExpirationDateChanged() {
        //updatedProduct.expirationDate = input
        presenter.updateProduct(updatedProduct)

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Handles the product update success state.
     *
     */
    override fun onProductUpdateSuccess() {
        product = updatedProduct
        updateViews()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Handles the product updating fail state.
     *
     */
    override fun onProductUpdateFail() {
        updatedProduct = product
        TODO("Display toast") //To change body of created functions use File | Settings | File Templates.
    }
}
