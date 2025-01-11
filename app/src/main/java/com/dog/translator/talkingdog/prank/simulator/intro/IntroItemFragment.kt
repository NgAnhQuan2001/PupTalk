package com.dog.translator.talkingdog.prank.simulator.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.databinding.FragmentIntroItemBinding

class IntroItemFragment : Fragment() {
    private var binding: FragmentIntroItemBinding? = null
    var imageSlider =
        intArrayOf(
            R.drawable.img_intro_slider_one,
            R.drawable.img_intro_slider_two,
            R.drawable.img_intro_slider_three
        )

    companion object {
        private const val PAGE_NUMBER: String = "PAGE_NUMBER"

        @JvmStatic
        fun newInstance(position: Int): IntroItemFragment {
            val fragment = IntroItemFragment()
            val bundle = Bundle()
            bundle.putInt(PAGE_NUMBER, position)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroItemBinding.inflate(layoutInflater)
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
    }
}
