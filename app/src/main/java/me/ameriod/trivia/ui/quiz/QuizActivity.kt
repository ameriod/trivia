package me.ameriod.trivia.ui.quiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
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
    }

    override fun createPresenter() = QuizPresenter.newInstance(intent.getParcelableExtra(QUIZ))

    override fun displayError(error: String) {
        // no op
    }

    override fun showProgress(show: Boolean) {
        // no op
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

    override fun setCompletedQuiz(quiz: Quiz) {
        setResult(Activity.RESULT_OK, Intent().putExtra(RESULT, quiz))
        finish()
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
        const val RESULT = "result"
        private const val QUIZ = "quiz"

        fun getLaunchIntent(context: Context,
                            quiz: Quiz): Intent =
                Intent(context, QuizActivity::class.java)
                        .putExtra(QUIZ, quiz)

    }
}