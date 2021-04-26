package com.example.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.animation.doOnEnd
import kotlin.math.roundToInt

class BtnLoadingProgressbar(view:View) {
    private val layout = view as LinearLayout
    private val progressbar: ProgressBar = view.findViewById(R.id.btn_loading_layout_progressbar)
    private val textView: TextView = view.findViewById(R.id.btn_loading_layout_tv)
    private val ivDone: ImageView = view.findViewById(R.id.btn_loading_layout_iv)

    fun setLoading(){
        layout.isEnabled = false
        textView.visibility = View.GONE
        progressbar.visibility = View.VISIBLE
        ivDone.visibility = View.GONE

        progressbar.scaleX = 1f
        ivDone.scaleX = 0f
    }

    fun setState(isSuccess:Boolean,onAnimationEnd:()->Unit){
        val v = layout.background
        val bgColor = if (isSuccess)
            Color.parseColor("#66BB6A") // color success
        else
            Color.parseColor("#E53935") // color error

        val bgAnim = ObjectAnimator.ofFloat(0f,1f).setDuration(600L)
        bgAnim.addUpdateListener {
            val mul = it.animatedValue as Float
            v.colorFilter = PorterDuffColorFilter(adjustAlpha(bgColor,mul),PorterDuff.Mode.SRC_ATOP)
        }
        bgAnim.start() // start background animation
        bgAnim.doOnEnd {
            if (isSuccess)
                flipProgressBar(R.drawable.ic_done,R.drawable.btn_loading_layout_bg_success){if (it) onAnimationEnd() }
            else
                flipProgressBar(R.drawable.ic_fail,R.drawable.btn_loading_layout_bg_error){ if (it) onAnimationEnd() }
        }
    }

    private fun flipProgressBar(img: Int, imageBackgroundDrawable: Int, isEnded:(Boolean)->Unit) {
        ivDone.setBackgroundResource(imageBackgroundDrawable)
        ivDone.setImageResource(img)

        val flip1 = ObjectAnimator.ofFloat(progressbar,"scaleX",1f,0f)
        val flip2 = ObjectAnimator.ofFloat(ivDone,"scaleX",0f,1f)
        flip1.duration = 400
        flip2.duration = 400
        flip1.interpolator = DecelerateInterpolator()
        flip2.interpolator = AccelerateDecelerateInterpolator()
        flip1.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                progressbar.visibility = View.GONE
                ivDone.visibility = View.VISIBLE
                flip2.start()
            }
        })
        flip1.start() // start from progressbar to half
        flip2.doOnEnd { isEnded(true) }
    }

    fun reset(){
        textView.visibility = View.VISIBLE
        progressbar.visibility = View.GONE
        ivDone.visibility = View.GONE
        progressbar.scaleX = 1f
        ivDone.scaleX = 0f
        layout.background.clearColorFilter()
        layout.isEnabled = true
    }

    private fun adjustAlpha(bgColor: Int, mul: Float): Int {
        val a = (Color.alpha(bgColor)*mul).roundToInt()
        val r = Color.red(bgColor)
        val g = Color.green(bgColor)
        val b = Color.blue(bgColor)
        return Color.argb(a,r,g,b)
    }
}