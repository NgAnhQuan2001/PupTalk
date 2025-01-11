package com.dog.translator.talkingdog.prank.simulator.translator

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dog.translator.talkingdog.prank.simulator.DialogUtil
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.databinding.FragmentTranslatorBinding
import com.dog.translator.talkingdog.prank.simulator.language.SystemUtil
import java.io.File
import java.io.IOException
import kotlin.random.Random


class TranslatorFragment : Fragment() {
    private var dialog: Dialog? = null
    private var handler: Handler? = null
    private var runable: Runnable? = null
    private var binding: FragmentTranslatorBinding? = null
    private var isRecording = false
    private var isHuman = true
    private var mediaPlayer: MediaPlayer? = null
    private var mediaRecorder: MediaRecorder? = null
    private var fileName: String? = null
    private val mHelpGuid: ArrayList<TranslatorUIModel> = ArrayList()
    private val listDogToHuman: ArrayList<TranslatorUIModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTranslatorBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpListener()
    }

    private fun initView() {
        getPermissionToRecordAudio()
        initialize()
        setUpDataDogToHuman()

    }

    private fun setUpListener() {
        binding?.ivSwitchHuman?.setOnClickListener {
            binding?.ivDog?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_human
                )
            )
            binding?.ivHuman?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_dog
                )
            )
            binding?.ivSwitchDog?.visibility = View.VISIBLE
            binding?.ivSwitchHuman?.visibility = View.GONE
            isHuman = false
        }

        binding?.ivSwitchDog?.setOnClickListener {
            binding?.ivDog?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_dog
                )
            )
            binding?.ivHuman?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_human
                )
            )
            binding?.ivSwitchDog?.visibility = View.GONE
            binding?.ivSwitchHuman?.visibility = View.VISIBLE
            isHuman = true
        }

        binding?.ivVoice?.setOnClickListener {

            if (!isRecording) {
                isRecording = true
                try {
                    prepareRecording(true)
                    startRecording()

                } catch (e: Exception) {
                    prepareRecording(false)
                    stopRecording()
                    Toast.makeText(
                        requireContext(),
                        "Please give permission to your app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                prepareRecording(false)
                setUpDataRandom(isHuman)
            }
        }
    }

    private fun startRecording() {

        this.mediaRecorder = MediaRecorder()
        this.mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        this.mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        val root = Environment.getExternalStorageDirectory()
        val file = File(root.absolutePath + "/dogTranslate/Audios")
        if (!file.exists()) {
            file.mkdirs()
        }

        fileName = root.absolutePath + "/dogTranslate/Audios/" + (System.currentTimeMillis()
            .toString() + ".mp3")

        this.mediaRecorder!!.setOutputFile(fileName)
        this.mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            this.mediaRecorder!!.prepare()
            this.mediaRecorder!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // making the imageView a stop button starting the chronometer
        binding?.chronometer?.base = SystemClock.elapsedRealtime()
        binding?.chronometer?.start()
    }

    private fun setUpDataRandom(isHuman: Boolean) {
        if (isHuman) {
            handleDataRandomWithDogSpeak()
        } else {
            handleDataRandomWithHumanSpeak()
        }
    }

    private fun handleDataRandomWithHumanSpeak() {
        val itemData = Random.nextInt(listDogToHuman.size)
        val currentItem = listDogToHuman[itemData]

        dialog = DialogUtil.showConfirmationDialog(requireActivity(),
            title = currentItem.title,
            isCancelable = false,
            isCanceledOnTouchOutside = false,
            image = currentItem.image ?: R.drawable.ic_sound_argry,
            btnCloseClicked = {
                stopRecording()
                dialog!!.dismiss()
            })
        dialog!!.show()
        try {
            mediaPlayer = MediaPlayer()
            val descriptor: AssetFileDescriptor = context?.assets!!.openFd(
                currentItem.soundsByMP3 + "_" + SystemUtil.getPreLanguage(context) + ".mp3"
            )
            mediaPlayer?.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()
            mediaPlayer?.prepare()
            mediaPlayer?.start()

            mediaPlayer!!.setOnCompletionListener {
                stopRecording()
            }
        } catch (e: Exception) {

        }
    }

    private fun handleDataRandomWithDogSpeak() {
        val itemData = Random.nextInt(mHelpGuid.size)
        val currentItem = mHelpGuid[itemData]
        dialog = DialogUtil.showConfirmationDialog(requireActivity(),
            title = currentItem.title,
            isCancelable = false,
            isCanceledOnTouchOutside = false,
            image = currentItem.image ?: R.drawable.ic_sound_argry,
            btnCloseClicked = {
                stopRecording()
            })
        mediaPlayer =
            MediaPlayer.create(requireContext(), currentItem.sounds ?: R.raw.sound_1)
        mediaPlayer?.start()
        mediaPlayer!!.setOnCompletionListener { stopRecording() }
    }

    private fun prepareRecording(isRecording: Boolean) {
        if (isRecording) {
            binding?.ivWave?.visibility = View.VISIBLE
            binding?.ivLineGrab?.visibility = View.GONE
            binding?.ivWave?.let {
                Glide.with(requireContext()).load(R.drawable.img_wave_form)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(it)
            }
            binding?.ivVoice?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_play_record
                )
            )
        } else {
            binding?.ivWave?.visibility = View.GONE
            binding?.ivLineGrab?.visibility = View.VISIBLE
            binding?.ivVoice?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_record
                )
            )
        }
    }

    private fun stopRecording() {
        isRecording = false
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }

            mediaPlayer!!.release()
            mediaPlayer = MediaPlayer()
            if (dialog != null) {
                dialog?.dismiss()
            }
            mediaRecorder?.stop()
            mediaRecorder?.reset()
            mediaRecorder?.release()
            mediaRecorder = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        this.mediaRecorder = null
        //starting the chronometer
        binding?.chronometer?.stop()
        binding?.chronometer?.base = SystemClock.elapsedRealtime()
        binding?.ivWave?.visibility = View.GONE
        binding?.ivLineGrab?.visibility = View.VISIBLE
        binding?.ivVoice?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_record
            )
        )
    }

    private fun getPermissionToRecordAudio() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_AUDIO_PERMISSION_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.size == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // some thing
            }
        }
    }

    private fun initialize() {
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_hi,
                resources.getString(R.string.sound_item_1),
                R.raw.dog_hi
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_wonder,
                resources.getString(R.string.sound_item_2),
                R.raw.dog_wonder
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_happy,
                resources.getString(R.string.sound_item_3),
                R.raw.dog_happy
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_love,
                resources.getString(R.string.sound_item_4),
                R.raw.dog_love
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_happy_walk,
                resources.getString(R.string.sound_item_5),
                R.raw.dog_happy_walk
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_dance,
                resources.getString(R.string.sound_item_6),
                R.raw.dog_dance
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_hand_clap,
                resources.getString(R.string.sound_item_8),
                R.raw.dog_handclap
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_hi_fence,
                resources.getString(R.string.sound_item_9),
                R.raw.dog_hi_fence
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_cry,
                resources.getString(R.string.sound_item_10),
                R.raw.dog_cry
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_cry_lying,
                resources.getString(R.string.sound_item_11),
                R.raw.dog_crying
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_no,
                resources.getString(R.string.sound_item_12),
                R.raw.dog_no
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_yes,
                resources.getString(R.string.sound_item_13),
                R.raw.dog_yeah
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_wow,
                resources.getString(R.string.sound_item_14),
                R.raw.dog_wow
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_pet,
                resources.getString(R.string.sound_item_15),
                R.raw.dog_pet
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_begging,
                resources.getString(R.string.sound_item_16),
                R.raw.dog_begging
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_want_sleep,
                resources.getString(R.string.sound_item_17),
                R.raw.dog_want_sleep
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_yeah,
                resources.getString(R.string.sound_item_18),
                R.raw.dog_yeah
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_lie,
                resources.getString(R.string.sound_item_19),
                R.raw.dog_lie
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_startle,
                resources.getString(R.string.sound_item_20),
                R.raw.dog_startle
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_scratch,
                resources.getString(R.string.sound_item_21),
                R.raw.dog_scrath
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_sick,
                resources.getString(R.string.sound_item_22),
                R.raw.dog_scared
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_exhausted,
                resources.getString(R.string.sound_item_23),
                R.raw.dog_exhausted
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_scare,
                resources.getString(R.string.sound_item_24),
                R.raw.dog_scared
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_argry,
                resources.getString(R.string.sound_item_25),
                R.raw.dog_angry
            )
        )
        mHelpGuid.add(
            TranslatorUIModel(
                R.drawable.ic_sound_super_argry,
                resources.getString(R.string.sound_item_26),
                R.raw.dog_super_angry
            )
        )
    }

    private fun setUpDataDogToHuman() {
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_hi,
                resources.getString(R.string.label_hi),
                R.raw.sound_1,
                "Hi_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_wonder,
                getString(R.string.label_doing),
                R.raw.sound_2,
                "Wonder_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_happy,
                resources.getString(R.string.label_happy),
                R.raw.sound_3,
                "Happy_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_love,
                resources.getString(R.string.label_love),
                R.raw.sound_4,
                "Love_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_happy_walk,
                resources.getString(R.string.label_happy_walk),
                R.raw.sound_5,
                "Happy walk_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_dance,
                resources.getString(R.string.label_dance),
                R.raw.sound_6,
                "Dance_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_hand_clap,
                resources.getString(R.string.label_hand_clap),
                R.raw.sound_7,
                "Hand-Clap_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_hi_fence,
                resources.getString(R.string.label_hi_fence),
                R.raw.sound_8,
                "Hi fence_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_cry,
                resources.getString(R.string.label_sound_cry),
                R.raw.sound_10,
                "Cry_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_cry_lying,
                resources.getString(R.string.label_sound_cry_lying),
                R.raw.sound_11,
                "Cry lying_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_no,
                resources.getString(R.string.sound_item_12),
                R.raw.sound_12,
                "No_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_yes,
                resources.getString(R.string.label_sound_yes),
                R.raw.sound_13,
                "Yes_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_wow,
                resources.getString(R.string.label_sound_wow),
                R.raw.sound_14,
                "Wow_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_pet,
                resources.getString(R.string.label_sound_pet),
                R.raw.sound_15,
                "Pet_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_begging,
                resources.getString(R.string.label_sound_begging),
                R.raw.sound_1,
                "Begging_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_want_sleep,
                resources.getString(R.string.label_sound_want_sleep),
                R.raw.sound_2,
                "Want Sleep_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_yeah,
                resources.getString(R.string.label_sound_yeah),
                R.raw.sound_3,
                "Yeah_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_lie,
                resources.getString(R.string.label_sound_lie),
                R.raw.sound_4,
                "Lie_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_startle,
                resources.getString(R.string.label_sound_startle),
                R.raw.sound_5,
                "Startled_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_scratch,
                resources.getString(R.string.label_sound_scratch),
                R.raw.sound_6,
                "Scratch_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_sick,
                resources.getString(R.string.label_sound_shy),
                R.raw.sound_7,
                "Want Sleep_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_exhausted,
                resources.getString(R.string.label_sound_exhausted),
                R.raw.sound_8,
                "Exhausted_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_scare,
                resources.getString(R.string.label_sound_scare),
                R.raw.sound_9,
                "Scared_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_argry,
                resources.getString(R.string.label_sound_argry),
                R.raw.sound_10,
                "Angry_hunam"
            )
        )
        listDogToHuman.add(
            TranslatorUIModel(
                R.drawable.ic_sound_super_argry,
                resources.getString(R.string.label_sound_super_argry),
                R.raw.sound_11,
                "Super Angry_hunam"
            )
        )
    }

    override fun onStop() {
        if (dialog != null) {
            dialog?.dismiss()
        }
        if (handler != null && runable != null) {
            handler!!.removeCallbacks(runable!!)
        }
        stopRecording()
        super.onStop()
    }

    companion object {
        const val REQUEST_AUDIO_PERMISSION_CODE = 101
    }
}