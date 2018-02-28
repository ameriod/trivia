package me.ameriod.trivia

import android.app.Application
import me.ameriod.trivia.api.OpenTriviaRepository
import timber.log.Timber

class TriviaApplication : Application() {

    val repository: OpenTriviaRepository by lazy {
        OpenTriviaRepository(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}