package me.ameriod.trivia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*
import me.ameriod.trivia.ui.filter.FilterController
import me.ameriod.trivia.ui.history.HistoryController
import me.ameriod.trivia.ui.play.PlayController


class MainActivity : AppCompatActivity() {

    private lateinit var router: Router
    private var selectedMenuId: Int = R.id.action_play

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        router = Conductor.attachRouter(this, changeHandler, savedInstanceState)

        selectedMenuId = savedInstanceState?.getInt(OUT_SELECTED, R.id.action_play) ?: R.id.action_play

        bottomNavigationView.setOnNavigationItemSelectedListener {
            val selectedId = it.itemId
            if (router.hasRootController() && selectedMenuId == selectedId) {
                false
            } else {
                when (selectedId) {
                    R.id.action_play -> {
                        router.setRoot(RouterTransaction.with(PlayController.newInstance()))
                    }
                    R.id.action_history -> {
                        router.setRoot(RouterTransaction.with(HistoryController.newInstance()))
                    }
                    R.id.action_filter -> {
                        router.setRoot(RouterTransaction.with(FilterController.newInstance()))
                    }
                }
                selectedMenuId = selectedId
                true
            }
        }
        bottomNavigationView.selectedItemId = selectedMenuId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(OUT_SELECTED, selectedMenuId)
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    companion object {
        private const val OUT_SELECTED = "out_selected"
    }
}
