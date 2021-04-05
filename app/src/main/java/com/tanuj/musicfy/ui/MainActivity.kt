package com.tanuj.musicfy.ui

import androidx.viewpager.widget.ViewPager

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tanuj.musicfy.R
import com.tanuj.musicfy.data.Song
import com.tanuj.musicfy.data.SongDatabase
import com.tanuj.musicfy.exoplayer.isPlaying
import com.tanuj.musicfy.exoplayer.toSong
import com.tanuj.musicfy.ui.adapters.SongAdapter
import com.tanuj.musicfy.ui.viewmodels.MainViewModel
import com.tanuj.musicfy.ui.viewmodels.SongViewModel
import com.tanuj.musicfy.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

private const val NUM_PAGES = 3

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var songAdapter: SongAdapter

    lateinit var mainViewModel : MainViewModel

    private val songViewModel: SongViewModel by viewModels()

    private var playbackState: PlaybackStateCompat? = null

    private var curPlayingSong: Song? = null

    private var shouldUpdateSeekbar = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        view_pager.adapter = pagerAdapter

        if (curPlayingSong == null) curPlayingSong = SongDatabase.getAllSongs()[0]

        setUpRecyclerView()
        subscribeToObservers()

        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }

        songAdapter.songs = SongDatabase.getAllSongs()

        cancel_sliding_drawer.setOnClickListener {
            drawer.animateClose()
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                button_group.setPosition(position, true)
            }

            override fun onPageScrollStateChanged(state: Int) = Unit
        })

        button_group.setOnPositionChangedListener {
            view_pager.setCurrentItem(it)
        }

        iv_play_pause.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })

        iv_next.setOnClickListener {
            mainViewModel.skipToNextSong()
        }

        iv_forward.setOnClickListener {
            mainViewModel.forwardSong()
        }

        iv_previous.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }

        iv_rewind.setOnClickListener {
            mainViewModel.rewindSong()
        }

    }

    private fun setUpRecyclerView()  = recycler_view.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(context)
    }

    private fun subscribeToObservers() {
        mainViewModel.curPlayingSong.observe(this) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
        }

        mainViewModel.playbackState.observe(this) {
            playbackState = it
            iv_play_pause.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
            seekbar.progress = it?.position?.toInt() ?: 0

        }

        mainViewModel.isConnected.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.ERROR -> Snackbar.make(
                        rootLayout,
                        result.message ?: "An unknown error occured",
                        Snackbar.LENGTH_LONG
                    ).show()
                    else -> Unit
                }
            }
        }

        songViewModel.curPlayerPosition.observe(this) {
            if(shouldUpdateSeekbar) {
                seekbar.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }

        songViewModel.curSongDuration.observe(this) {
            seekbar.max = it.toInt()
            var minutes : Int = ((it/1000)/60).toInt()
            if (minutes < 0) minutes = 0
            val seconds : Int = (it/1000).toInt() % 60
            val secondsStr = if (seconds < 10) "0$seconds" else "$seconds"
            tv_song_duration.text = "$minutes:$secondsStr"
        }

    }

    private fun setCurPlayerTimeToTextView(ms: Long) {
        val minutes : Int = ((ms/1000)/60).toInt()
        val seconds : Int = (ms/1000).toInt() % 60
        val secondsStr = if (seconds < 10) "0$seconds" else "$seconds"
        tv_current_time.text = "$minutes:$secondsStr"
    }

    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 ->  MusicFragment()
                1 ->  LyricsFragment()
                2 ->  VisualizationFragment()
                else ->  MusicFragment()
            }
        }
    }
}