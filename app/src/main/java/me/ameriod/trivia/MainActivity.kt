package me.ameriod.trivia

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.ameriod.trivia.api.TriviaRepository
import timber.log.Timber
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*
import me.ameriod.trivia.ui.filter.FilterController
import me.ameriod.trivia.ui.question.QuestionController


class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        router = Conductor.attachRouter(this, changeHandler, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(FilterController.newInstance()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
