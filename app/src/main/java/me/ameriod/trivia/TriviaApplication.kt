package me.ameriod.trivia

import android.app.Application
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class TriviaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin{
            androidLogger(if (BuildConfig.DEBUG) Level.INFO else Level.ERROR)

            androidContext(this@TriviaApplication)

            modules(appModule)
        }
    }

}