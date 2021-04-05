package com.tanuj.musicfy.data

import android.content.Context
import android.util.Log
import java.lang.NumberFormatException

class LyricsSource {

    companion object {
        fun getAllLyrics(context: Context) : List<Lyrics> {
            return listOf(
                getLyrics(context, "We're Good.txt", "1"),
                getLyrics(context, "Peaches.txt", "2"),
                getLyrics(context, "Astronaut in the Ocean.txt", "3")
            )
        }

        private fun getLyrics(context: Context, fileName : String, mediaId : String) : Lyrics {
            val fileString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val fileLines : List<String> = fileString.split("\n")

            val lyric : MutableList<Lyric> = mutableListOf()

            for (line in fileLines) {
                val timeMinutesStr = line.substring(1, 3)
                val timeSecondsStr = line.substring(4,6)

                try {
                    val timeInMillis : Long = ((timeMinutesStr.toInt() * 60 + timeSecondsStr.toInt()) * 1000).toLong()
                    val subTitleStr = line.substring(10)
                    lyric.add(Lyric(timeInMillis, subTitleStr))
                }
                catch (nfe : NumberFormatException) {
                    Log.e("LyricsSource", nfe.message)
                }
            }

            return Lyrics(mediaId, lyric)
        }

    }

}