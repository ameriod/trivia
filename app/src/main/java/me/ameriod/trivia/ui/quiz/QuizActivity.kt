package me.ameriod.trivia.ui.quiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import kotlinx.android.synthetic.main.activity_quiz.*
import me.ameriod.lib.mvp.view.MvpAppCompatActivity
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.quiz.question.Answer
import me.ameriod.trivia.ui.quiz.question.Question
import me.ameriod.trivia.ui.quiz.question.QuestionController
import me.ameriod.trivia.ui.result.ResultActivity
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class QuizActivity : MvpAppCompatActivity<QuizContract.View, QuizContract.Presenter>(),
        QuizContract.View, QuestionController.OnQuestionAnsweredListener {

    private lateinit var router: Router

    // kotlin messes up the lint
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setSupportActionBar(toolbar)
        title = null
        router = Conductor.attachRouter(this, changeHandler, savedInstanceState)
        getPresenter().getInitialQuestion()
        getPresenter().startQuizTimer()
    }

    override fun createPresenter(): QuizContract.Presenter {
        val quiz = intent.getParcelableExtra<Quiz>(QUIZ)
                ?: throw IllegalArgumentException("Error need to pass in a quiz")
        return get { parametersOf(quiz) }
    }

    override fun displayError(error: String) {
        // no op
    }

    override fun showProgress(show: Boolean) {
        // no op
    }

    override fun onTimeUpdated(formattedTime: String) {
        quizTimer.text = formattedTime
    }

    override fun setCurrentQuestion(question: Question, isLastQuestion: Boolean) {
        val controller = QuestionController.newInstance(question, isLastQuestion)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction
                    .with(controller))
        } else {
            router.replaceTopController(RouterTransaction
                    .with(controller)
                    .popChangeHandler(HorizontalChangeHandler())
                    .pushChangeHandler(HorizontalChangeHandler()))
        }
    }

    override fun setProgress(currentPosition: Int, total: Int) {
        quizProgress.text = resources.getQuantityString(R.plurals.quiz_progress, total,
                currentPosition, total)
    }

    override fun setCompletedQuiz(resultId: Long) {
        startActivity(ResultActivity.getLaunchIntent(this, resultId))
        setResult(Activity.RESULT_OK, Intent().putExtra(RESULT, resultId))
        ActivityCompat.finishAfterTransition(this)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle(R.string.questions_back_dialog_title)
                .setMessage(R.string.questions_back_dialog_message)
                .setPositiveButton(R.string.questions_back_btn_positive, { _, _ ->
                    if (!router.handleBack()) {
                        setResult(Activity.RESULT_CANCELED)
                        super.onBackPressed()
                    }
                })
                .setNegativeButton(R.string.questions_back_btn_negative, null)
                .show()
    }

    override fun onQuestionAnswered(answer: Answer, question: Question) {
        getPresenter().getNextQuestion(answer)
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