package com.dog.translator.talkingdog.prank.simulator.language

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dog.translator.talkingdog.prank.simulator.HomeActivity
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.base.SharePrefUtils
import com.dog.translator.talkingdog.prank.simulator.intro.IntroActivity
import com.dog.translator.talkingdog.prank.simulator.language.adapter.LanguageAdapter
import kotlin.system.exitProcess


class LanguageScreenActivity : AppCompatActivity(), IClickLanguage {
    private var adapter: LanguageAdapter? = null
    private var model: LanguageModel = LanguageModel()
    private var sharedPreferences: SharedPreferences? = null
    private var currentApiVersion = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        currentApiVersion = Build.VERSION.SDK_INT
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        // load and show ads native language
        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)


        adapter = LanguageAdapter(this, setLanguageDefault(), this)
        findViewById<RecyclerView>(R.id.rcl_language).adapter = adapter
        if (intent.getStringExtra("TYPE_SCREEN").equals("HOME")) {
            findViewById<ImageView>(R.id.iv_back).visibility = View.VISIBLE
        } else {
            findViewById<ImageView>(R.id.iv_back).visibility = View.GONE
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onClick(data: LanguageModel) {
        adapter?.setSelectLanguage(data)
        model = data
    }

    fun setLanguageDefault(): List<LanguageModel>? {
        val lists: MutableList<LanguageModel> = ArrayList()
        val key: String = SystemUtil.getPreLanguage(this)
        lists.add(
            LanguageModel(
                this.resources.getString(R.string.language_en),
                "en",
                false,
                R.drawable.ic_english
            )
        )
        lists.add(LanguageModel("Portugal", "pt", false, R.drawable.ic_portugal))
        lists.add(
            LanguageModel(
                this.resources.getString(R.string.language_fr),
                "fr",
                false,
                R.drawable.ic_france
            )
        )
        lists.add(LanguageModel("Tiếng Việt", "vi", false, R.drawable.ic_vietnam)) // Thêm dòng này
        lists.add(LanguageModel("Spanish", "es", false, R.drawable.ic_spanish))
        lists.add(LanguageModel("India", "hi", false, R.drawable.ic_india))
        Log.e("", "setLanguageDefault: $key")
        for (i in lists.indices) {
            if (!sharedPreferences!!.getBoolean("nativeLanguage", false)) {
                if (key == lists[i].isoLanguage) {
                    val data = lists[i]
                    data.isCheck = true
                    lists.remove(lists[i])
                    lists.add(0, data)
                    break
                }
            } else {
                if (key == lists[i].isoLanguage) {
                    lists[i].isCheck = true
                }
            }
        }
        return lists
    }

    fun ivDone(v: View) {
        if (model != null) {
            SystemUtil.setPreLanguage(this@LanguageScreenActivity, model.isoLanguage)
        }
        val editor = getSharedPreferences("MY_PRE", MODE_PRIVATE).edit()
        editor.putBoolean("nativeLanguage", true)
        editor.apply()
        SystemUtil.setLocale(this)
        if (intent.getStringExtra("TYPE_SCREEN").equals("HOME")) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        } else {
            SharePrefUtils.forceLanguage(this@LanguageScreenActivity)
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }
        finish()
    }


    fun ivBack(v: View) {
        if (intent.getStringExtra("TYPE_SCREEN").equals("HOME")) {
            finishAndRemoveTask()
        } else {
            finishAffinity()
            exitProcess(0)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
        exitProcess(0)
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus
        if (v != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
            v is EditText &&
            !v.javaClass.name.startsWith("android.webkit.")
        ) {
            val sourceCoordinates = IntArray(2)
            v.getLocationOnScreen(sourceCoordinates)
            val x = ev.rawX + v.getLeft() - sourceCoordinates[0]
            val y = ev.rawY + v.getTop() - sourceCoordinates[1]
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                hideKeyboard(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null) {
            activity.window.decorView
            val imm: InputMethodManager =
                activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
            }
        }
    }


}