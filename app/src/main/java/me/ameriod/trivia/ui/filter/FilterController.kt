package me.ameriod.trivia.ui.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.controller_filter.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty
import me.ameriod.trivia.mvvm.MvvmController
import me.ameriod.trivia.mvvm.viewModel
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.quiz.Quiz
import me.ameriod.trivia.ui.quiz.QuizActivity


class FilterController(args: Bundle) : MvvmController(args), View.OnClickListener,
        AdapterView.OnItemSelectedListener, TriviaBaseAdapter.OnItemClickListener {

    private val viewModel: FilterViewModel by viewModel()
    private var snackbar: Snackbar? = null

    private val difficultyAdapter: DifficultyAdapter by lazy {
        DifficultyAdapter(activity!!)
    }

    private val categoryAdapter: TriviaBaseAdapter<OtCategory> by lazy {
        TriviaBaseAdapter<OtCategory>(activity!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_filter, container, false)
        v.filterBtnStart.setOnClickListener(this)
        v.filterDifficultySpinner.adapter = difficultyAdapter
        v.filterDifficultySpinner.onItemSelectedListener = this

        v.filterCategoriesRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(v.context)
        v.filterCategoriesRecycler.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(v.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
        v.filterCategoriesRecycler.adapter = categoryAdapter

        // Setup the seek bar
        v.filterCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // un-zero index
                if (fromUser) {
                    val display = (progress + 1).toString()
                    v.filterCountEt.setText(display)
                    v.filterCountEt.setSelection(display.length)
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

                // getPresenter().setCount(input)
            }
        })
        return v
    }

    private fun setCategories(categories: List<OtCategory>, selectedItem: OtCategory) {
        categoryAdapter.setItems(categories)
        categoryAdapter.setSingleSelected(selectedItem)
    }

    private fun setDifficulties(difficulties: List<OtDifficulty>, selectedItem: OtDifficulty) {
        difficultyAdapter.setItems(difficulties)
        view!!.filterDifficultySpinner.setSelection(difficultyAdapter.getPositionForItem(selectedItem))
    }

    private fun setQuestionCount(count: String) {
        val v = view!!
        // the edit text will set the seek bar
        v.filterCountEt.setText(count)
        v.filterCountEt.setSelection(count.length)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        subscribeIo(viewModel.stateSubject, ::setState)
        viewModel.getFilters()
    }

    private fun setState(state: FilterViewModel.State) {
        view?.apply {
            when (state) {
                is FilterViewModel.State.Loading -> {
                    filterLoading.isVisible = state.show
                }
                is FilterViewModel.State.Error -> {
                    snackbar = Snackbar.make(this, state.message, Snackbar.LENGTH_INDEFINITE)
                            .setAction(state.actionText) {
                                state.action
                            }
                    snackbar?.show()
                }
                is FilterViewModel.State.Loaded -> {
                    setCategories(state.categories, state.selectedFilter.category)
                    setDifficulties(state.difficulties, state.selectedFilter.difficulty)
                    setQuestionCount(state.selectedFilter.count.toString())
                }
                is FilterViewModel.State.QuizLoaded -> {
                    setQuiz(state.quiz)
                }
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // no op
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //  getPresenter().setDifficulty(difficultyAdapter.getItem(position))
        dropKeyboard()
    }

    override fun onClick(v: View) {
        when (v) {
            view?.filterBtnStart -> viewModel.getQuiz()
        }
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val category = categoryAdapter.getItem(position)
        categoryAdapter.setSingleSelected(category)
        //  getPresenter().setCategory(category)
        dropKeyboard()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            viewModel.resetFilter()
            dropKeyboard()
        }
    }

    private fun setQuiz(quiz: Quiz) {
        if (quiz.isQuizDone()) {
            Snackbar.make(view!!, R.string.filter_no_more, Snackbar.LENGTH_SHORT).show()
        } else {
            startActivityForResult(QuizActivity.getLaunchIntent(activity!!, quiz), REQUEST_CODE)
        }
    }

    private fun dropKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    companion object {
        const val REQUEST_CODE = 1000

        @JvmStatic
        fun newInstance() = FilterController(Bundle.EMPTY)
    }
}