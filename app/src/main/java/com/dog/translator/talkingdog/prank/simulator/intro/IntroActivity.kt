package com.dog.translator.talkingdog.prank.simulator.intro

import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.base.BaseActivity
import com.dog.translator.talkingdog.prank.simulator.databinding.ActivityIntroBinding
import kotlin.system.exitProcess

class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    override fun initView() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, IntroFragment.newInstance())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun getViewBinding(): ActivityIntroBinding = ActivityIntroBinding.inflate(layoutInflater)


    override fun onBackPressed() {
        finishAffinity()
        exitProcess(1)
        super.onBackPressed()
    }

}