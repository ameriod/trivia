package me.ameriod.trivia.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.ameriod.lib.mvp.Mvp
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.api.OpenTriviaRepository
import me.ameriod.trivia.api.OpenTriviaService
import me.ameriod.trivia.api.db.TriviaDatabase
import me.ameriod.trivia.ui.filter.FilterViewModel
import me.ameriod.trivia.ui.history.HistoryViewModel
import me.ameriod.trivia.ui.quiz.Quiz
import me.ameriod.trivia.ui.quiz.QuizViewModel
import me.ameriod.trivia.ui.result.ResultContract
import me.ameriod.trivia.ui.result.ResultInteractor
import me.ameriod.trivia.ui.result.ResultPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

val appModule = module {

    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("API").d(message)
            }
        })
        logging.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
    }
    single<Gson> {
        GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
    }

    single<OpenTriviaService> {
        Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .client(get())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(get()))
                .build()
                .create(OpenTriviaService::class.java)
    }

    single<TriviaDatabase> {
        Room.databaseBuilder(androidContext(),
                TriviaDatabase::class.java, "results")
                .build()
    }

    single {
        val triviaDb: TriviaDatabase = get()
        triviaDb.resultDao()
    }

    single<OpenTriviaRepository> {
        OpenTriviaRepository(
                context = androidContext(),
                service = get(),
                triviaDao = get()
        )
    }

    single<Mvp.ErrorHandler> {
        object : Mvp.ErrorHandler {
            override fun onError(e: Throwable): String {
                Timber.e(e, "Error in MVP")
                return ""
            }
        }
    }


    single<IObservableSchedulerRx2> {
        IObservableSchedulerRx2.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN
    }

    single<IObservableSchedulerRx2>(named(name = RxSchedulerName.SUBSCRIBE_COMPUTATION_OBSERVE_ANDROID_MAIN.name)) {
        IObservableSchedulerRx2.SUBSCRIBE_COMPUTATION_OBSERVE_ANDROID_MAIN
    }

    viewModel {
        FilterViewModel(androidContext(), get(), get())
    }

    viewModel {
        HistoryViewModel(androidContext(), get(), get())
    }

    viewModel {
        val quiz: Quiz = it.get<Quiz>() as Quiz
        QuizViewModel(get(), quiz)
    }

    single<ResultContract.Interactor> {
        ResultInteractor(get())
    }

    single<ResultContract.Presenter> {
        ResultPresenter(get(), get(), get())
    }

}

enum class RxSchedulerName {
    COMPUTATION,
    IO,
    SUBSCRIBE_COMPUTATION_OBSERVE_ANDROID_MAIN,
    SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
    TRAMPOLINE
}
