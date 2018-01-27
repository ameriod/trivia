package me.ameriod.trivia

import android.app.Application
import timber.log.Timber

class TriviaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}