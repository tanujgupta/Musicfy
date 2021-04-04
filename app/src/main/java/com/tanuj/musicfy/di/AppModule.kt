package com.tanuj.musicfy.di

import android.content.Context
import com.tanuj.musicfy.exoplayer.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesMusicServiceConnection(
        @ApplicationContext context : Context
    ) = MusicServiceConnection(context)

}