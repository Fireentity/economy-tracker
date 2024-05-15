package it.unipd.dei.music_application.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.unipd.dei.music_application.App

@Module
@InstallIn(SingletonComponent::class)
object MyApplicationModule {

    @Provides
    fun providesMainApplicationInstance(@ApplicationContext context: Context): App {
        return context as App
    }
}