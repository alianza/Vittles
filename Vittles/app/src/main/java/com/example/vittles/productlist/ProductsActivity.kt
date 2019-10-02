package com.example.vittles.productlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.example.domain.model.Product
import com.example.vittles.R
import com.example.vittles.VittlesApp
import com.example.vittles.mvp.BaseActivity
import com.example.vittles.productadd.AddProductActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

/**
 * Activity class for the main activity. This is the activity that shows the list of products.
 *
 * @author Arjen Simons
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
class ProductsActivity : BaseActivity() {

    @Inject lateinit var presenter: ProductsPresenter

    private var products = arrayListOf<Product>()
    private val productAdapter = ProductAdapter(products)

    /**
     * Called when the ProductsActivity is created.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        ButterKnife.bind(this)
        with(presenter) {
            start(this@ProductsActivity)
        }
        initViews()
    }

     override fun injectDependencies() {
        DaggerProductsComponent.builder()
            .appComponent(VittlesApp.component)
            .productsModule(ProductsModule())
            .build()
            .inject(this)
    }

    /**
     * Called when the mainActivity starts.
     * Re-populates the RecyclerView.
     */
    override fun onStart() {
        super.onStart()
        populateRecyclerView()
    }

    /**
     * Called when the option menu is created.
     *.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Handles action bar item clicks.
     *
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Called when the add button is clicked.
     * It starts the addProduct activity.
     */
    private fun onAddButtonClick() {
        val addProductActivityIntent = Intent(
            this,
            AddProductActivity::class.java
        )
        startActivity(addProductActivityIntent)
    }

    /**
     * Initializes the RecyclerView.
     */
    private fun initViews(){
        
        fab.setOnClickListener { onAddButtonClick() }

        rvProducts.layoutManager =
            LinearLayoutManager(this@ProductsActivity, RecyclerView.VERTICAL, false)
        rvProducts.adapter = productAdapter

//        populateRecyclerView()
    }

    /**
     * Populates the RecyclerView with items from the local DataBase.
     */
    private fun populateRecyclerView(){

        products.clear()

        presenter.loadProducts()

//        for (i in fetchProductUseCase.fetch().subscribe()) {
//            products.add(i)
//        }


    }

    fun onProductsLoad(products: List<Product>) {
        this.products.addAll(products)
        productAdapter.notifyDataSetChanged()
    }

    fun onProductsLoadFail() {
        println("FAIL")
    }
}
