package com.dog.translator.talkingdog.prank.simulator.whistle

import android.annotation.SuppressLint
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.audiofx.Equalizer
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import com.dog.translator.talkingdog.prank.simulator.R
import com.dog.translator.talkingdog.prank.simulator.databinding.FragmentWhistleBinding
import java.util.*


class WhistleFragment : Fragment() {

    private var binding: FragmentWhistleBinding? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var progress: Int = 0
    private var lowerEqualizerBandLevel: Short = 0
    private var mEqualizer: Equalizer? = null
    private var handler: Handler? = null
    private var time:Long= 0L
    var stubBand: Short = 1500
    var band0: Short = 1500
    var band1: Short = 1500
    var band2: Short = 1500
    var band3: Short = 1500
    var band4: Short = 1500

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWhistleBinding.inflate(layoutInflater)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        handleInteract()

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initData() {
        mediaPlayer = MediaPlayer()
        val descriptor: AssetFileDescriptor = context?.assets!!.openFd("whistle_sound.mp3")
        mediaPlayer?.setDataSource(
            descriptor.fileDescriptor,
            descriptor.startOffset,
            descriptor.length
        )
        descriptor.close()
        mEqualizer = Equalizer(0, mediaPlayer!!.audioSessionId)
        mEqualizer?.enabled = true
        lowerEqualizerBandLevel = mEqualizer!!.bandLevelRange[0]
        val maxEQLevel = mEqualizer!!.bandLevelRange[1]
        mEqualizer?.setBandLevel(0, band0)
        mEqualizer?.setBandLevel(1, band1)
        mEqualizer?.setBandLevel(2, band2)
        mEqualizer?.setBandLevel(3, band3)
        mEqualizer?.setBandLevel(4, band4)
        binding?.seekbar?.min = 20
        binding?.seekbar?.max = 20000
        binding?.seekbar?.progress = 20
        binding?.textView?.text = "20 Hz"
        changeHz(isPlaying)
        binding?.seekbar?.isEnabled = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleInteract() {
        binding?.seekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (mEqualizer != null) {
                    binding?.textView?.text = "$p1 Hz"
                    changeHz(false)
                    progress = p1
                    if (p1 in 20..3000) {
                        band0 = if (p1 <= 1500) {
                            (1500 - p1).toShort()
                        } else {
                            (-(p1 - 1500)).toShort()
                        }
                        band1 = 1500
                        band2 = 1500
                        band3 = 1500
                        band4 = 1500
                    } else if (p1 in 3001..6000) {
                        band0 = -1500
                        stubBand = (p1 - 3000).toShort()
                        band1 = if (stubBand <= 1500) {
                            (1500 - stubBand).toShort()
                        } else {
                            (-(stubBand - 1500)).toShort()
                        }
                        band2 = 1500
                        band3 = 1500
                        band4 = 1500
                    } else if (p1 in 6001..9000) {
                        band0 = -1500
                        band1 = -1500
                        stubBand = (p1 - 6000).toShort()
                        band2 = if (stubBand <= 1500) {
                            (1500 - stubBand).toShort()
                        } else {
                            (-(stubBand - 1500)).toShort()
                        }
                        band3 = 1500
                        band4 = 1500
                    } else if (p1 in 9001..12000) {
                        band0 = -1500
                        band1 = -1500
                        band2 = -1500
                        stubBand = (p1 - 9000).toShort()
                        band3 = if (stubBand <= 1500) {
                            (1500 - stubBand).toShort()
                        } else {
                            (-(stubBand - 1500)).toShort()
                        }
                        band4 = -1500
                    } else if (p1 in 12001..15000) {
                        band0 = -1500
                        band1 = -1500
                        band2 = -1500
                        band3 = -1500
                        stubBand = (p1 - 12000).toShort()
                        band4 = if (stubBand <= 1500) {
                            (1500 - stubBand).toShort()
                        } else {
                            (-(stubBand - 1500)).toShort()
                        }
                    } else {
                        band0 = 0
                        band1 = 0
                        band2 = 0
                        band3 = 0
                        band4 = 8000
                    }
                    try {
                        mEqualizer?.setBandLevel(0, band0)
                        mEqualizer?.setBandLevel(1, band1)
                        mEqualizer?.setBandLevel(2, band2)
                        mEqualizer?.setBandLevel(3, band3)
                        mEqualizer?.setBandLevel(4, band4)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        binding?.appCompatImageView?.setOnClickListener {
            if (SystemClock.elapsedRealtime() - time < 1000){
                return@setOnClickListener;
            }
            time = SystemClock.elapsedRealtime();
            isPlaying = !isPlaying
            changeHz(isPlaying)
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playBeep() {
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = MediaPlayer()
            }
            val descriptor: AssetFileDescriptor = context?.assets!!.openFd("whistle_sound.mp3")
            mediaPlayer?.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()
            mEqualizer = Equalizer(0, mediaPlayer!!.audioSessionId)
            mEqualizer?.enabled = true
            lowerEqualizerBandLevel = mEqualizer!!.bandLevelRange[0]
            val maxEQLevel = mEqualizer!!.bandLevelRange[1]
            mEqualizer?.setBandLevel(0, band0)
            mEqualizer?.setBandLevel(1, band1)
            mEqualizer?.setBandLevel(2, band2)
            mEqualizer?.setBandLevel(3, band3)
            mEqualizer?.setBandLevel(4, band4)
            binding?.seekbar?.min = 20
            binding?.seekbar?.max = 20000
            binding?.seekbar?.progress = progress

            mediaPlayer?.prepare()
            mediaPlayer?.setVolume(1f, 1f)
            mediaPlayer?.isLooping = true
            mediaPlayer?.playbackParams = mediaPlayer?.playbackParams!!.setSpeed(0.6f)
            mediaPlayer?.playbackParams = mediaPlayer?.playbackParams!!.setPitch(1f)
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopPlay() {
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }

            mediaPlayer!!.release()
            handler = null
            mediaPlayer = MediaPlayer()

            progress = 20
            if (mEqualizer != null) {
                mEqualizer!!.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    fun changeHz(isPlay: Boolean) {
        if (isPlay) {
            binding?.seekbar?.isEnabled = false
            binding?.appCompatImageView?.setImageResource(R.drawable.ic_pause)
            playBeep()
        } else {
            binding?.seekbar?.isEnabled = true
            binding?.appCompatImageView?.setImageResource(R.drawable.ic_play)
            stopPlay()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            isPlaying = !isPlaying
            changeHz(isPlaying)
        }
        super.onPause()
    }
}