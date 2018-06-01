package me.ameriod.trivia.ui.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*
import me.ameriod.trivia.R

class ResultActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        setSupportActionBar(toolbar)

        router = Conductor.attachRouter(this, changeHandler, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(ResultController
                    .newInstance(intent.getLongExtra(RESULT_ID, 0L))))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    companion object {

        private const val RESULT_ID = "result_id"

        @JvmStatic
        fun getLaunchIntent(context: Context, resultId: Long): Intent =
                Intent(context, ResultActivity::class.java)
                        .putExtra(RESULT_ID, resultId)

    }
}