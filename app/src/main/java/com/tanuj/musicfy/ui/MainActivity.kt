package com.tanuj.musicfy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanuj.musicfy.R
import com.tanuj.musicfy.data.SongDatabase
import com.tanuj.musicfy.ui.adapters.SongAdapter
import com.tanuj.musicfy.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val NUM_PAGES = 3

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var songAdapter: SongAdapter

    lateinit var mainViewModel : MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        view_pager.adapter = pagerAdapter

        setUpRecyclerView()

        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }

        songAdapter.songs = SongDatabase.getAllSongs()

    }

    private fun setUpRecyclerView()  = recycler_view.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(context)
    }

    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 ->  FirstFragment()
                1 ->  SecondFragment()
                2 ->  ThirdFragment()
                else ->  FirstFragment()
            }
        }
    }
}