package com.tanuj.musicfy.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.tanuj.musicfy.R
import com.tanuj.musicfy.util.Constants.NOTIFICATION_CHANNEL_ID
import com.tanuj.musicfy.util.Constants.NOTIFICATION_ID
import java.lang.NumberFormatException

private const val TAG = "MusicNotificationMgr"

class MusicNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newSongCallback: () -> Unit
) {

    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            NOTIFICATION_CHANNEL_ID,
            R.string.notification_channel_name,
            R.string.notification_channel_description,
            NOTIFICATION_ID,
            DescriptionAdapter(mediaController),
            notificationListener
        ).apply {
            setSmallIcon(R.drawable.ic_music)
            setMediaSessionToken(sessionToken)
        }
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    private inner class DescriptionAdapter(
        private val mediaController: MediaControllerCompat
    ) : PlayerNotificationManager.MediaDescriptionAdapter {

        override fun getCurrentContentTitle(player: Player): CharSequence {
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {

            try {
                val bitmapResource : Int = mediaController.metadata.description.iconUri.toString().toInt()
                return BitmapFactory.decodeResource(context.resources, bitmapResource)
            } catch (nfe : NumberFormatException) {
                Log.d(TAG,nfe.message)
            }

            return null
        }
    }
}
