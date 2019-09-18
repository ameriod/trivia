package me.ameriod.trivia.ui.quiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_quiz.*
import me.ameriod.trivia.R
import me.ameriod.trivia.mvvm.IObservableSchedulerRx2
import me.ameriod.trivia.ui.quiz.question.Answer
import me.ameriod.trivia.ui.quiz.question.Question
import me.ameriod.trivia.ui.quiz.question.QuestionController
import me.ameriod.trivia.ui.result.ResultActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class QuizActivity : AppCompatActivity(),
        QuestionController.OnQuestionAnsweredListener {

    private lateinit var disposable: CompositeDisposable
    private lateinit var router: Router
    private val viewModel: QuizViewModel by viewModel {
        val quiz = intent.getParcelableExtra<Quiz>(QUIZ)
                ?: throw IllegalArgumentException("Error need to pass in a quiz")
        parametersOf(quiz)
    }
    private val schedulerRx2: IObservableSchedulerRx2 by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setSupportActionBar(toolbar)
        title = null
        router = Conductor.attachRouter(this, changeHandler, savedInstanceState)

        disposable = CompositeDisposable()

        disposable.add(viewModel.getStateObservable()
                .compose(schedulerRx2.schedule())
                .subscribe({
                    setState(it)
                }, {
                    Timber.e(it, "Error with quiz flow")
                }))

        viewModel.getInitialQuestion()
        viewModel.startQuizTimer()

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun setState(state: QuizViewModel.State) {
        when (state) {
            is QuizViewModel.State.DisplayQuestion -> onDisplayQuestion(state)
            is QuizViewModel.State.OnQuizFinished -> onQuizCompleted(state)
            is QuizViewModel.State.OnTimeUpdated -> onTimeUpdated(state)
        }
    }

    private fun onTimeUpdated(state: QuizViewModel.State.OnTimeUpdated) {
        quizTimer.text = state.formattedTime
    }

    private fun onDisplayQuestion(state: QuizViewModel.State.DisplayQuestion) {
        val controller = QuestionController.newInstance(state.question, state.isLastQuestion)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction
                    .with(controller))
        } else {
            router.replaceTopController(RouterTransaction
                    .with(controller)
                    .popChangeHandler(HorizontalChangeHandler())
                    .pushChangeHandler(HorizontalChangeHandler()))
        }

        quizProgress.text = resources.getQuantityString(R.plurals.quiz_progress,
                state.totalQuestions,
                state.currentPosition,
                state.totalQuestions)
    }

    private fun onQuizCompleted(state: QuizViewModel.State.OnQuizFinished) {
        startActivity(ResultActivity.getLaunchIntent(this, state.resultId))
        setResult(Activity.RESULT_OK, Intent().putExtra(RESULT, state.resultId))
        ActivityCompat.finishAfterTransition(this)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle(R.string.questions_back_dialog_title)
                .setMessage(R.string.questions_back_dialog_message)
                .setPositiveButton(R.string.questions_back_btn_positive) { _, _ ->
                    if (!router.handleBack()) {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                }
                .setNegativeButton(R.string.questions_back_btn_negative, null)
                .show()
    }

    override fun onQuestionAnswered(answer: Answer, question: Question) {
        viewModel.onQuestionAnswered(answer)
    }

    companion object {
        const val RESULT = "result_id"
        private const val QUIZ = "quiz"

        fun getLaunchIntent(context: Context,
                            quiz: Quiz): Intent =
                Intent(context, QuizActivity::class.java)
                        .putExtra(QUIZ, quiz)

    }
}