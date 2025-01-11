package com.dog.translator.talkingdog.prank.simulator.sounds


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.os.postDelayed
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.base.BaseActivity
import com.dog.translator.talkingdog.prank.simulator.databinding.ActivitySoundItemBinding
import com.dog.translator.talkingdog.prank.simulator.language.KeyValue
import com.dog.translator.talkingdog.prank.simulator.training.TrainingModel
import com.google.gson.Gson


class SoundItemActivity : BaseActivity<ActivitySoundItemBinding>() {

    var mHandler: Handler? = null
    private val mHelpGuid: ArrayList<TrainingModel> = ArrayList()
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var isLoop: Boolean = false
    private var handlerTime: Handler? = null
    private var currentSong: Int? = null
    private var currentTimer: KeyValue? = null
    private var countDownTimer: CountDownTimer? = null
    private var isCountDown: Boolean? = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        mediaPlayer = MediaPlayer()
        handlerTime = Handler(Looper.getMainLooper())
        initData()
    }

    override fun getViewBinding(): ActivitySoundItemBinding =
        ActivitySoundItemBinding.inflate(layoutInflater)

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initData() {
        mHandler = Handler(Looper.getMainLooper())
        binding?.seekbar?.isEnabled = false
        binding.switchCompat.isChecked = isLoop
        val type = intent.getIntExtra("TYPE", 0)
        val trainingModel =
            Gson().fromJson(intent.extras?.getString("OBJECT"), TrainingModel::class.java)
        if (trainingModel != null) {
            binding.title.text = trainingModel.title
            binding.imgView.setBackgroundResource(trainingModel.img)
            currentSong = trainingModel.idSound
        }

        binding.play.setImageResource(R.drawable.ic_play_sound)

        binding.switchCompat.setOnCheckedChangeListener { p0, p1 ->
            isLoop = p1
//            stopPlay()
        }
        val years = ArrayList<KeyValue>()
        years.add(KeyValue("1", this.resources.getString(R.string.sound_off)))
        years.add(KeyValue("2", "15s"))
        years.add(KeyValue("3", "30s"))
        years.add(KeyValue("4", "45s"))
        years.add(KeyValue("5", "1 " + this.resources.getString(R.string.sound_min)))
        years.add(KeyValue("6", "2 " + this.resources.getString(R.string.sound_min)))
        years.add(KeyValue("7", "5 " + this.resources.getString(R.string.sound_min)))

        currentTimer = years[0]
        val langAdapter = ArrayAdapter<KeyValue>(this, R.layout.item_spinner, years)
        langAdapter.setDropDownViewResource(R.layout.item_spinner)
        binding.spinner.adapter = langAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            Runnable {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                currentTimer = years[position]
//                if (years[position].key == "1"){
//                    binding.linearLayout.visibility = View.GONE
//                } else {
//                    binding.linearLayout.visibility = View.VISIBLE
//                    binding.timeSet.text = years[position].value
//                }

                stopPlay()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }

            override fun run() {
                playBeep(currentSong!!)
            }
        }

        binding.play.setOnClickListener {
            if (!isPlaying) {
                handlerCountDownTimer(trainingModel.idSound)
            } else {
                if (isCountDown!!) {
                    binding.timeSet.text = ""
                    countDownTimer!!.cancel()
                }
                stopPlay()
            }
//            isPlaying = !isPlaying

        }

        binding.icBack.setOnClickListener {
            finishAndRemoveTask()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlerCountDownTimer(sound: Int) {
        isPlaying = true
        binding.linearLayout.visibility = View.VISIBLE
        binding.play.setImageResource(R.drawable.ic_sound_pause)
        val time = currentTimer!!.key
        val duration = when (time) {
            "2" -> 15000L
            "3" -> 30000L
            "4" -> 45000L
            "5" -> 60000L
            "6" -> 120000L
            "7" -> 300000L
            else -> 0L
        }

        if (duration > 0L) {
            countDownTimer = object : CountDownTimer(duration, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(p0: Long) {
                    isCountDown = true
                    binding.timeSet.text = (p0 / 1000).toString() + "s"

                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFinish() {
                    isCountDown = false
                    isPlaying = true
                    playBeep(sound)
                }

            }
            countDownTimer?.start()
        } else {
            isPlaying = true
            isCountDown = false
            playBeep(sound)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun playBeep(song: Int) {
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = MediaPlayer()
            }
            mediaPlayer = MediaPlayer.create(this, song)
            mediaPlayer?.isLooping = isLoop
//            mediaPlayer?.playbackParams = mediaPlayer?.playbackParams!!.setSpeed(0.6f)
//            mediaPlayer?.playbackParams = mediaPlayer?.playbackParams!!.setPitch(1f)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                stopPlay()
                binding.seekbar.progress = 0
            }
            binding.play.setImageResource(R.drawable.ic_sound_pause)
            binding.seekbar.max = mediaPlayer!!.duration / 200
            this@SoundItemActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                            val mCurrentPosition: Int = mediaPlayer!!.currentPosition / 200
                            binding.seekbar.progress = mCurrentPosition
                        mHandler?.postDelayed(this, 200)
                    } else {
                        stopPlay()
                        mHandler?.removeCallbacks(this)
//                        binding?.play?.setImageResource(R.drawable.ic_play_sound)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopPlay() {
        isPlaying = false
        binding.timeSet.text = currentTimer!!.value
        if (countDownTimer != null)
            countDownTimer!!.cancel()
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }

            mediaPlayer!!.release()
            mediaPlayer = MediaPlayer()
            binding?.play?.setImageResource(R.drawable.ic_play_sound)

            binding?.seekbar?.progress = 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        isCountDown = false
        isPlaying = false
        stopPlay()
        countDownTimer?.cancel()
        super.onPause()
    }


}