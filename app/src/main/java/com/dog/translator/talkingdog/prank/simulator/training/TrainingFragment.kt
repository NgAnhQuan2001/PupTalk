package com.dog.translator.talkingdog.prank.simulator.training

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.databinding.FragmentTrainingBinding



class TrainingFragment : Fragment() {

    private val mHelpGuid: ArrayList<TrainingModel> = ArrayList()
    private var binding: FragmentTrainingBinding? = null
    private var trainingAdapter: TrainingAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTrainingBinding.inflate(layoutInflater)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        handleInteract()

    }

    private fun initData() {
        mHelpGuid.add(
            TrainingModel(
                R.drawable.ic_item,
                resources.getString(R.string.training_food_title),
                resources.getString(R.string.training_food_description),
                R.drawable.img_food
            )
        )

        mHelpGuid.add(
            TrainingModel(
                R.drawable.ic_preaise,
                resources.getString(R.string.training_praise_title),
                resources.getString(R.string.training_praise_description),
                R.drawable.img_praise
            )
        )

        mHelpGuid.add(
            TrainingModel(
                R.drawable.ic_bitting,
                resources.getString(R.string.training_bitting_title),
                resources.getString(R.string.training_bitting_description),
                R.drawable.img_biting
            )
        )

        mHelpGuid.add(
            TrainingModel(
                R.drawable.ic_obdience,
                resources.getString(R.string.training_obdience_title),
                resources.getString(R.string.training_obdience_description),
                R.drawable.img_obdience
            )
        )

        mHelpGuid.add(
            TrainingModel(
                R.drawable.ic_barking,
                resources.getString(R.string.training_barking_title),
                resources.getString(R.string.training_barking_description),
                R.drawable.img_barking
            )
        )


        binding!!.rclView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            trainingAdapter = TrainingAdapter(
                mHelpGuid, context
            ) { pos, trainingModel ->
                val intent = Intent(context, TrainingItemActivity::class.java)
                intent.putExtra("TYPE",pos)
                intent.putExtra("OBJECT", trainingModel)
                startActivity(intent)
            }
            adapter = trainingAdapter
        }
    }

    private fun handleInteract() {
    }

    override fun onPause() {
        super.onPause()
    }

}