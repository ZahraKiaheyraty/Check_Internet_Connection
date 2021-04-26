package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_btn.setOnClickListener {
            val progressbar = BtnLoadingProgressbar(it) // `it` is view of button
            progressbar.setLoading()
            handler.postDelayed({
                progressbar.setState(true){ // executed after animation end
                    handler.postDelayed({
                        startError(progressbar)
                    },1500)
                }
            },2000)
        }
    }

    private fun startError(progressbar: BtnLoadingProgressbar) {
        progressbar.reset()
        handler.postDelayed({
            progressbar.setLoading()
            handler.postDelayed({
                progressbar.setState(false){ // executed after animation end
                    handler.postDelayed({
                        progressbar.reset()
                    },1500)
                }
            },2000)
        },600)
    }
}
