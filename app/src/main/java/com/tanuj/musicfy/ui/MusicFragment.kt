package com.tanuj.musicfy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tanuj.musicfy.R
import com.tanuj.musicfy.data.Song
import com.tanuj.musicfy.data.SongDatabase
import com.tanuj.musicfy.exoplayer.toSong
import com.tanuj.musicfy.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_music.*
import java.lang.NumberFormatException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MusicFragment : Fragment() {

    lateinit var mainViewModel : MainViewModel

    private var curPlayingSong: Song? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

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

       try {
           val songImagResource = curPlayingSong!!.imageUrl.toInt()
           song_image.setImageResource(songImagResource)
       }
       catch (nfe : NumberFormatException) {
           Log.d("MusicFragment ", nfe.message)
       }
        title.text = curPlayingSong!!.title
        artist.text = curPlayingSong!!.artist
    }
}