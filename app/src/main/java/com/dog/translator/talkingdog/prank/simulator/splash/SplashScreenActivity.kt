package com.dog.translator.talkingdog.prank.simulator.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import com.dog.translator.talkingdog.prank.simulator.HomeActivity
import com.dog.translator.talkingdog.prank.simulator.base.BaseActivity
import com.dog.translator.talkingdog.prank.simulator.base.SharePrefUtils
import com.dog.translator.talkingdog.prank.simulator.databinding.ActivitySplashBinding
import com.dog.translator.talkingdog.prank.simulator.language.LanguageScreenActivity
import kotlin.system.exitProcess

class SplashScreenActivity : BaseActivity<ActivitySplashBinding>() {
    private var countDownTimer: CountDownTimer? = null
    private var countIndex = 0
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun initView() {
        sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        countDownTimer = object : CountDownTimer(TIMER_DURATION, TIMER_COUNT_DOWN) {
            override fun onTick(p0: Long) {
                countIndex++
                binding.progressBar.progress =
                    (countIndex * 100) / (TIMER_DURATION / TIMER_COUNT_DOWN).toInt()
            }

            override fun onFinish() {
                countIndex++
                binding.progressBar.progress = 100

                if (!SharePrefUtils.isLanguage(this@SplashScreenActivity)) {
                    val intent =
                        Intent(this@SplashScreenActivity, LanguageScreenActivity::class.java)
                    intent.putExtra("TYPE_SCREEN", "SPLASH")
                    startActivity(intent)
                }else
                {
                    val intent =
                        Intent(this@SplashScreenActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }

        }
        countDownTimer?.start()
    }

    override fun onBackPressed() {
        finishAffinity()
        exitProcess(1)
        super.onBackPressed()
    }

    companion object {
        const val TIMER_DURATION = 5000L
        const val TIMER_COUNT_DOWN = 1000L
    }

}