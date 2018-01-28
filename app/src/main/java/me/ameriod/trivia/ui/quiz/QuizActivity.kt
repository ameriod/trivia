package me.ameriod.trivia.ui.quiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import kotlinx.android.synthetic.main.activity_quiz.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Question
import me.ameriod.trivia.ui.result.QuizResult

class QuizActivity : AppCompatActivity(), QuestionController.OnQuestionAnsweredListener {

    private lateinit var router: Router
    private lateinit var questions: List<Question>
    private lateinit var answers: MutableList<String>
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setSupportActionBar(toolbar)
        title = null

        questions = intent.getParcelableArrayListExtra(QUESTIONS)

        // restore the state or default values
        position = savedInstanceState?.getInt(OUT_STATE_POSITION, 0) ?: 0
        answers = savedInstanceState?.getStringArrayList(OUT_STATE_ANSWERS) ?: mutableListOf()

        router = Conductor.attachRouter(this, changeHandler, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(QuestionController.newInstance(questions[0], isLastQuestion())))
        }
        updateProgress()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(OUT_STATE_POSITION, position)
        outState?.putStringArrayList(OUT_STATE_ANSWERS, ArrayList(answers))
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

    override fun onQuestionAnswered(answer: String, question: Question) {
        position++
        answers.add(answer)

        updateProgress()

        if (isQuizComplete()) {
            val results = QuizResult(answers, questions)
            setResult(Activity.RESULT_OK, Intent()
                    .putExtra(RESULT, results))
            finish()
        } else {
            router.replaceTopController(RouterTransaction.with(QuestionController.newInstance(questions[position], isLastQuestion()))
                    .popChangeHandler(HorizontalChangeHandler())
                    .pushChangeHandler(HorizontalChangeHandler()))
        }
    }

    private fun updateProgress() {
        val answeredQuestions = answers.size + 1
        val questionCount = questions.size
        quizProgress.text = resources.getQuantityString(R.plurals.quiz_progress, questionCount, answeredQuestions, questionCount)
    }

    private fun isLastQuestion(): Boolean {
        return position > questions.size
    }

    private fun isQuizComplete(): Boolean {
        return position >= questions.size
    }

    companion object {
        const val RESULT = "result"
        private const val QUESTIONS = "questions"

        private const val OUT_STATE_POSITION = "out_position"
        private const val OUT_STATE_ANSWERS = "out_answers"

        fun getLaunchIntent(context: Context,
                            questions: List<Question>): Intent =
                Intent(context, QuizActivity::class.java)
                        .putExtra(QUESTIONS, ArrayList(questions))

    }
}