package com.dog.translator.talkingdog.prank.simulator.training

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.base.BaseActivity
import com.dog.translator.talkingdog.prank.simulator.databinding.ActivityTrainingItemBinding
import com.dog.translator.talkingdog.prank.simulator.databinding.FragmentTrainingBinding


class TrainingItemActivity : BaseActivity<ActivityTrainingItemBinding>() {

    private val mHelpGuid: ArrayList<TrainingModel> = ArrayList()
    private var trainingAdapter: TrainingItemAdapter? = null
    override fun initView() {
        initData()
    }

    override fun getViewBinding(): ActivityTrainingItemBinding =
        ActivityTrainingItemBinding.inflate(layoutInflater)

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }




    private fun initData() {
        val type = intent.getIntExtra("TYPE", 0)
        val trainingModel = intent.extras?.getSerializable("OBJECT") as? TrainingModel
        if (trainingModel != null) {
            binding.title.text = trainingModel.title
            binding.ivStatus.setImageDrawable(ContextCompat.getDrawable(this,trainingModel.imageDetail))
        }
        when (type) {
            0 -> {
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_food_1_title),
                        resources.getString(R.string.activity_training_food_1_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_food_2_title),
                        resources.getString(R.string.activity_training_food_2_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_food_3_title),
                        resources.getString(R.string.activity_training_food_3_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_food_4_title),
                        resources.getString(R.string.activity_training_food_4_description),0
                    )
                )
            }
            1 -> {
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_praise_1_title),
                        resources.getString(R.string.activity_training_praise_1_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_praise_2_title),
                        resources.getString(R.string.activity_training_praise_2_description),0
                    )
                )
            }
            2 -> {
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_bitting_1_title),
                        resources.getString(R.string.activity_training_bitting_1_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_bitting_2_title),
                        resources.getString(R.string.activity_training_bitting_2_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_bitting_3_title),
                        resources.getString(R.string.activity_training_bitting_3_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_bitting_4_title),
                        resources.getString(R.string.activity_training_bitting_4_description),0
                    )
                )
            }
            3 -> {
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_obdience_1_title),
                        resources.getString(R.string.activity_training_obdience_1_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_obdience_2_title),
                        resources.getString(R.string.activity_training_obdience_2_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_obdience_3_title),
                        resources.getString(R.string.activity_training_obdience_3_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_obdience_4_title),
                        resources.getString(R.string.activity_training_obdience_4_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_obdience_5_title),
                        resources.getString(R.string.activity_training_obdience_5_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_obdience_6_title),
                        resources.getString(R.string.activity_training_obdience_6_description),0
                    )
                )
            }
            4 -> {
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_barking_1_title),
                        resources.getString(R.string.activity_training_barking_1_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_barking_2_title),
                        resources.getString(R.string.activity_training_barking_2_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_barking_3_title),
                        resources.getString(R.string.activity_training_barking_3_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_barking_4_title),
                        resources.getString(R.string.activity_training_barking_4_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_barking_5_title),
                        resources.getString(R.string.activity_training_barking_5_description),0
                    )
                )
                mHelpGuid.add(
                    TrainingModel(
                        0,
                        resources.getString(R.string.activity_training_barking_6_title),
                        resources.getString(R.string.activity_training_barking_6_description),0
                    )
                )
            }
        }

//
        binding!!.rclView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            trainingAdapter = TrainingItemAdapter(
                mHelpGuid, context
            ) { pos, trainingModel -> Log.d("Tuyen", "Tuyen") }
            adapter = trainingAdapter
        }

        binding.icBack.setOnClickListener {
            finishAndRemoveTask()
        }
    }

}