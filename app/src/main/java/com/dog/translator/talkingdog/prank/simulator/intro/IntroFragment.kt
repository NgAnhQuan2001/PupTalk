package com.dog.translator.talkingdog.prank.simulator.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.dog.translator.talkingdog.prank.simulator.HomeActivity
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private var mAdapter: ViewSliderAdapter? = null
    private var binding: FragmentIntroBinding? = null
    private var imageSlider =
        intArrayOf(
            R.drawable.img_intro_slider_one,
            R.drawable.img_intro_slider_two,
            R.drawable.img_intro_slider_three
        )
    private var indicator: Array<ImageView>? = null
    private var page: Int = 0


    companion object {
        private const val PAGE_NUMBER: String = "PAGE_NUMBER"

        @JvmStatic
        fun newInstance(): IntroFragment {
            return IntroFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroBinding.inflate(layoutInflater)
        binding?.ivSlider?.setBackgroundResource(
            imageSlider[arguments?.getInt(PAGE_NUMBER) ?: 0]
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        updateIndicatorPage(0)
        indicator = arrayOf(binding!!.ivIndicatorOne, binding!!.ivIndicatorTwo, binding!!.ivIndicatorThree)
        mAdapter = ViewSliderAdapter(this)
        binding?.viewPagerSlider?.adapter = mAdapter
        binding?.viewPagerSlider?.currentItem = page
        binding?.viewPagerSlider?.clipToPadding = false
        binding?.viewPagerSlider?.clipChildren = false
        updateIndicatorPage(page)
//
        binding?.viewPagerSlider?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                page = position
                if (position >= 2) {
                    binding?.llNext?.setOnClickListener {
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                    }
                }
                updateIndicatorPage(page)
            }
        })

        binding?.llNext?.setOnClickListener {
            page += 1
            binding?.viewPagerSlider?.setCurrentItem(page, true)
        }
    }

    private fun updateIndicatorPage(position: Int) {
        indicator?.let {
            for (i in it.indices) {
                it[i].setBackgroundResource(
                    if (i == position) R.drawable.bg_selected_indicator else R.drawable.bg_unselected_indicator
                )
            }
        }
    }
}
