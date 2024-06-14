package it.unipd.dei.jetpack_compose_frontend

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
@Module
@InstallIn(SingletonComponent::class)
class App: Application() {

    @Provides
    @Singleton
    fun provideApp(): App {
        return this;
    }

}