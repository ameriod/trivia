package me.ameriod.trivia.ui.filter

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.Spinner
import com.bluelinelabs.conductor.Controller
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.controller_filter.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.TriviaRepository
import me.ameriod.trivia.ui.quiz.QuizActivity
import timber.log.Timber

class FilterController(args: Bundle) : Controller(args), View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private val adapter : FilterDifficultlyAdapter by lazy {
        FilterDifficultlyAdapter(activity!!, listOf("Any", "Easy", "Medium", "Hard"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_filter, container, false)
        v.filterBtnStart.setOnClickListener(this)

        // Setup the seek bar
        v.filterCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // un-zero index
                if (fromUser) {
                    v.filterCountEt.setText((progress + 1).toString())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // no op
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // no op
            }
        })

        v.filterCountEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // no op
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no op
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var input = 1
                if (s.isNotEmpty()) {
                    input = s.toString().toInt()
                }
                if (input <= 0) {
                    input = 1
                } else if (input > 50) {
                    input = 50
                }
                // set the progress
                v.filterCount.postDelayed({
                    v.filterCount.progress = input - 1
                }, 0)


            }
        })

        // difficulty
        v.filterDifficultySpinner.adapter = adapter
        v.filterDifficultySpinner.onItemSelectedListener = this

        return v
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // no op
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val difficulty = adapter.getItem(position)

    }

    override fun onClick(v: View) {
        val view = view!!
        when (v) {
            view.filterBtnStart -> startQuestions(v)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            // TODO clear filters
        }
    }

    private fun startQuestions(view: View) {
        TriviaRepository().getQuestions(QuizFilter(10, null, null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    startActivityForResult(QuizActivity.getLaunchIntent(view.context, response.results), REQUEST_CODE)
                }, { throwable ->
                    Timber.e(throwable, "Error")
                })
    }

    companion object {
        const val REQUEST_CODE = 1000

        @JvmStatic
        fun newInstance() = FilterController(Bundle.EMPTY)
    }
}