package com.tanuj.musicfy.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanuj.musicfy.R
import com.tanuj.musicfy.data.Lyrics
import com.tanuj.musicfy.data.LyricsSource
import com.tanuj.musicfy.data.Song
import com.tanuj.musicfy.data.SongDatabase
import com.tanuj.musicfy.exoplayer.toSong
import com.tanuj.musicfy.ui.adapters.LyricsAdapter
import com.tanuj.musicfy.ui.viewmodels.MainViewModel
import com.tanuj.musicfy.ui.viewmodels.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_lyrics.*
import javax.inject.Inject

@AndroidEntryPoint
class LyricsFragment : Fragment(R.layout.fragment_lyrics) {

    private lateinit var mainViewModel : MainViewModel

    private lateinit var sonViewModel : SongViewModel

    @Inject
    lateinit var lyricsAdapter: LyricsAdapter

    private var curPlayingSong: Song? = null

    private lateinit var lyricsList : List<Lyrics>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentViews()
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        sonViewModel = ViewModelProvider(requireActivity()).get(SongViewModel::class.java)

        lyricsList = LyricsSource.getAllLyrics(requireContext())

        setUpRecyclerView()
        subscribeToObservers()
    }

    private fun setUpRecyclerView() = recyler_view_lyrics.apply {
        adapter = lyricsAdapter
        layoutManager = LinearLayoutManager(context)
    }

    private fun subscribeToObservers() {
        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            curPlayingSong = it.toSong()

            setFragmentViews()
        }

        sonViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            lyricsAdapter.setCurrentLyricPosition((it/1000).toInt())

            recyler_view_lyrics?.smoothScrollToPosition(lyricsAdapter.curLyricPosition)
        }
    }

    private fun setFragmentViews() {
        if (curPlayingSong == null) curPlayingSong = SongDatabase.getAllSongs()[0]

        title.text = curPlayingSong!!.title
        artist.text = curPlayingSong!!.artist

        if (::lyricsList.isInitialized) {
            when(curPlayingSong!!.mediaId) {
                "1" -> lyricsAdapter.submitList(lyricsList.get(0).lyricItems, context)
                "2" -> lyricsAdapter.submitList(lyricsList.get(1).lyricItems, context)
                "3" -> lyricsAdapter.submitList(lyricsList.get(2).lyricItems, context)
            }
        }
    }
}