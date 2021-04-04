package com.tanuj.musicfy.ui

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
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
import com.tanuj.musicfy.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val NUM_PAGES = 3

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var songAdapter: SongAdapter

    lateinit var mainViewModel : MainViewModel

    private var playbackState: PlaybackStateCompat? = null

    private var curPlayingSong: Song? = null


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

        iv_play_pause.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
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

    }

    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 ->  MusicFragment()
                1 ->  SecondFragment()
                2 ->  ThirdFragment()
                else ->  MusicFragment()
            }
        }
    }
}