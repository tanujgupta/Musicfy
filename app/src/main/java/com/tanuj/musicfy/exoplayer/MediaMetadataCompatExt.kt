package com.tanuj.musicfy.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.tanuj.musicfy.data.Song

fun MediaMetadataCompat.toSong(): Song? {
    return description?.let {
        Song(
            it.mediaId ?: "",
            it.title.toString(),
            it.subtitle.toString(),
            it.mediaUri.toString(),
            it.iconUri.toString()
        )
    }
}