package com.example.vittles.productinfo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.domain.product.Product
import com.example.vittles.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject



class ProductInfoFragment : DaggerFragment(), ProductInfoContract.View {

    @Inject
    lateinit var presenter: ProductInfoPresenter

    lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.start(this@ProductInfoFragment)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun initViews() {

        val intent = activity!!.intent
        product = intent.getParcelableExtra(getString(R.string.product))
    }

    override fun onNameChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onExpirationDateChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
