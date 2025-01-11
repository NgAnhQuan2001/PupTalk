package com.dog.translator.talkingdog.prank.simulator.sounds

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.databinding.FragmentSoundsBinding
import com.dog.translator.talkingdog.prank.simulator.training.TrainingModel
import com.google.gson.Gson


class SoundFragment : Fragment() {

    private val mHelpGuid: ArrayList<TrainingModel> = ArrayList()
    private var binding: FragmentSoundsBinding? = null
    private var soundAdapter: SoundAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSoundsBinding.inflate(layoutInflater)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        handleInteract()

    }

    private fun initData() {

        initialize()
        binding!!.rclView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3)
            soundAdapter = SoundAdapter(
                mHelpGuid, context
            ) { pos, trainingModel ->
                val intent = Intent(context, SoundItemActivity::class.java)
                intent.putExtra("TYPE", pos)
                intent.putExtra("OBJECT", Gson().toJson(trainingModel))
                startActivity(intent)
            }
            adapter = soundAdapter
        }
    }

    private fun handleInteract() {
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initialize() {
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_hi, resources.getString(R.string.sound_item_1), R.raw.dog_hi))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_wonder, resources.getString(R.string.sound_item_2), R.raw.dog_wonder))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_happy, resources.getString(R.string.sound_item_3), R.raw.dog_happy))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_love, resources.getString(R.string.sound_item_4), R.raw.dog_love))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_happy_walk, resources.getString(R.string.sound_item_5), R.raw.dog_happy_walk))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_dance, resources.getString(R.string.sound_item_6), R.raw.dog_dance))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_hand_clap, resources.getString(R.string.sound_item_8), R.raw.dog_handclap))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_hi_fence, resources.getString(R.string.sound_item_9), R.raw.dog_hi_fence))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_cry, resources.getString(R.string.sound_item_10), R.raw.dog_cry))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_cry_lying, resources.getString(R.string.sound_item_11), R.raw.dog_crying))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_no, resources.getString(R.string.sound_item_12), R.raw.dog_no))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_yes, resources.getString(R.string.sound_item_13), R.raw.dog_yeah))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_wow, resources.getString(R.string.sound_item_14), R.raw.dog_wow))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_pet, resources.getString(R.string.sound_item_15), R.raw.dog_pet))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_begging, resources.getString(R.string.sound_item_16), R.raw.dog_begging))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_want_sleep, resources.getString(R.string.sound_item_17), R.raw.dog_want_sleep))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_yeah, resources.getString(R.string.sound_item_18), R.raw.dog_yeah))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_lie, resources.getString(R.string.sound_item_19), R.raw.dog_lie))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_startle, resources.getString(R.string.sound_item_20), R.raw.dog_startle))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_scratch, resources.getString(R.string.sound_item_21), R.raw.dog_scrath))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_sick, resources.getString(R.string.sound_item_22), R.raw.dog_scared))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_exhausted, resources.getString(R.string.sound_item_23), R.raw.dog_exhausted))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_scare, resources.getString(R.string.sound_item_24), R.raw.dog_scared))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_argry, resources.getString(R.string.sound_item_25), R.raw.dog_angry))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_super_argry, resources.getString(R.string.sound_item_26), R.raw.dog_super_angry))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_agree, resources.getString(R.string.sound_item_7), R.raw.dog_agree))
        mHelpGuid.add(TrainingModel(R.drawable.ic_sound_sad, resources.getString(R.string.sound_item_sad), R.raw.dog_sad))
    }

}