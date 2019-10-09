package com.example.vittles.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.vittles.R
import com.example.vittles.productlist.ProductsActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Use Handler to wait 1 second before opening the ProductsActivity.
        Handler().postDelayed({
            startActivity(
                Intent(
                    this@SplashActivity,
                    ProductsActivity::class.java
                )
            )
            finish()
        }, 1000)
    }
}
