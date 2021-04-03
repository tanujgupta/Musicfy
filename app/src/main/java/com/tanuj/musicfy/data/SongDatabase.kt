package com.tanuj.musicfy.data

import com.tanuj.musicfy.R

class SongDatabase {
    companion object {
        fun getAllSongs() : List<Song> {
            return listOf(
                Song("1", "We're Good", "Dua Lipa", "asset:///Dua Lipa - We're Good.mp3", R.drawable.we_re_good.toString()),
                Song("2", "Peaches", "Justin Bieber", "asset:///Justin Bieber - Peaches ft. Daniel Caesar, Giveon.mp3", R.drawable.peaches.toString()),
                Song("3", "Astronaut In The Ocean.mp3", "Masked Wolf", "asset:///Masked Wolf - Astronaut In The Ocean.mp3", R.drawable.astro.toString())
            )
        }
    }
}