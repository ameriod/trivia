package me.ameriod.trivia

import android.app.Application
import me.ameriod.trivia.api.TriviaRepository
import timber.log.Timber

class TriviaApplication : Application() {

    val repository: TriviaRepository by lazy {
        TriviaRepository(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}