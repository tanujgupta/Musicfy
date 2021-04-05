package com.tanuj.musicfy.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tanuj.musicfy.R
import com.tanuj.musicfy.data.Song
import com.tanuj.musicfy.data.SongDatabase
import com.tanuj.musicfy.exoplayer.toSong
import com.tanuj.musicfy.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_lyrics.*

class LyricsFragment : Fragment(R.layout.fragment_lyrics) {

    lateinit var mainViewModel : MainViewModel

    private var curPlayingSong: Song? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentViews()
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            curPlayingSong = it.toSong()

            setFragmentViews()
        }
    }

    private fun setFragmentViews() {
        if (curPlayingSong == null) curPlayingSong = SongDatabase.getAllSongs()[0]

        title.text = curPlayingSong!!.title
        artist.text = curPlayingSong!!.artist
    }
}