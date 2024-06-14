package it.unipd.dei.jetpack_compose_frontend.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.unipd.dei.jetpack_compose_frontend.App

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun providesMainApplicationInstance(@ApplicationContext context: Context): App {
        return context as App
    }
}