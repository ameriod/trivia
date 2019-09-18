package me.ameriod.trivia.ui.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.functions.Consumer
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
import java.util.concurrent.TimeUnit


class FilterController(args: Bundle) : MvvmController(args), View.OnClickListener,
        TriviaBaseAdapter.OnItemClickListener {

    private val viewModel: FilterViewModel by viewModel()
    private var snackbar: Snackbar? = null

    private val difficultyAdapter: DifficultyAdapter by lazy {
        DifficultyAdapter(activity!!)
    }

    private val categoryAdapter: TriviaBaseAdapter<OtCategory> by lazy {
        TriviaBaseAdapter<OtCategory>(activity!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.controller_filter, container, false).apply {
                filterBtnStart.setOnClickListener(this@FilterController)
                filterDifficultySpinner.adapter = difficultyAdapter

                filterCategoriesRecycler.layoutManager = LinearLayoutManager(context)
                filterCategoriesRecycler.addItemDecoration(DividerItemDecoration(context,
                        DividerItemDecoration.VERTICAL))
                filterCategoriesRecycler.adapter = categoryAdapter
            }

    override fun onAttach(view: View) {
        super.onAttach(view)
        subscribeIo(viewModel.getStateObservable()
                .distinctUntilChanged(), Consumer { setState(it) })

        viewModel.getFilters()

        subscribe(RxTextView.afterTextChangeEvents(view.filterCountEt)
                .skipInitialValue()
                .debounce(400, TimeUnit.MILLISECONDS)
                .map {
                    it.editable()?.toString()?.toIntOrNull() ?: 0
                }, viewModel.takeQuestionCount())

        subscribe(RxSeekBar.userChanges(view.filterCountSeekBar)
                .skipInitialValue()
                .debounce(400, TimeUnit.MILLISECONDS)
                // Seek bar is zero indexed
                .map { it + 1 }, viewModel.takeQuestionCount())

        subscribe(RxAdapterView.itemSelections(view.filterDifficultySpinner)
                .skipInitialValue()
                .map { difficultyAdapter.getItem(it) }, viewModel.takeDifficulty())

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

    private fun setCategories(categories: List<OtCategory>, selectedItem: OtCategory) {
        categoryAdapter.setItems(categories)
        categoryAdapter.setSingleSelected(selectedItem)
    }

    private fun setDifficulties(difficulties: List<OtDifficulty>, selectedItem: OtDifficulty) {
        difficultyAdapter.setItems(difficulties)
        view?.filterDifficultySpinner?.setSelection(difficultyAdapter.getPositionForItem(selectedItem))
    }

    private fun setQuestionCount(count: String) {
        view?.apply {
            // the edit text will set the seek bar
            filterCountEt.setText(count)
            filterCountEt.setSelection(count.length)
            // Set the seekbar
            filterCountSeekBar.postDelayed({
                filterCountSeekBar.progress = count.toInt() - 1
            }, 0)
        }
    }

    override fun onClick(v: View) {
        when (v) {
            view?.filterBtnStart -> viewModel.getQuiz()
        }
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val category = categoryAdapter.getItem(position)
        categoryAdapter.setSingleSelected(category)
        viewModel.setCategories(category)
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