package me.ameriod.trivia.ui.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*
import me.ameriod.trivia.R

class ResultActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        router = Conductor.attachRouter(this, changeHandler, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(ResultController
                    .newInstance(intent.getParcelableExtra(RESULT))))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    companion object {

        private const val RESULT = "result"

        @JvmStatic
        fun getLaunchIntent(context: Context, result: Result): Intent =
                Intent(context, ResultActivity::class.java)
                        .putExtra(RESULT, result)

    }
}