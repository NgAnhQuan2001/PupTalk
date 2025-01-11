package com.dog.translator.talkingdog.prank.simulator.base

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dog.translator.talkingdog.prank.simulator.DialogUtil
import com.dog.translator.talkingdog.prank.simulator.language.SystemUtil

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    private var currentApiVersion = 0
    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        currentApiVersion = Build.VERSION.SDK_INT
        val flags =
            ( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags
            val decorView = window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }
        SystemUtil.setLocale(this)
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = getViewBinding()
        setContentView(binding.root)
        initView()
    }

    override fun onStop() {
        DialogUtil.cancelDialog()
        super.onStop()

    }

    abstract fun initView()
    abstract fun getViewBinding(): B

}