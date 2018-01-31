package me.ameriod.trivia.ui.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.SeekBar
import kotlinx.android.synthetic.main.controller_filter.view.*
import me.ameriod.lib.mvp.view.MvpController
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.Difficulty
import me.ameriod.trivia.api.response.Question
import me.ameriod.trivia.ui.quiz.QuizActivity


class FilterController(args: Bundle) : MvpController<FilterContract.View, FilterContract.Presenter>(args), View.OnClickListener,
        AdapterView.OnItemSelectedListener, FilterContract.View, CategoryAdapter.OnItemClickListener {

    var snackbar: Snackbar? = null

    private val filterAdapter: FilterDifficultyAdapter by lazy {
        FilterDifficultyAdapter(activity!!)
    }

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter(activity!!, this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_filter, container, false)
        v.filterBtnStart.setOnClickListener(this)
        v.filterDifficultySpinner.adapter = filterAdapter
        v.filterDifficultySpinner.onItemSelectedListener = this

        v.filterCategoriesRecycler.layoutManager = LinearLayoutManager(v.context)
        v.filterCategoriesRecycler.addItemDecoration(DividerItemDecoration(v.context,
                DividerItemDecoration.VERTICAL))
        v.filterCategoriesRecycler.adapter = categoryAdapter

        return v
    }

    override fun onPostCreateView(view: View, container: ViewGroup) {
        super.onPostCreateView(view, container)
        // Setup the seek bar
        view.filterCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // un-zero index
                if (fromUser) {
                    val display = (progress + 1).toString()
                    view.filterCountEt.setText(display)
                    view.filterCountEt.setSelection(display.length)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // no op
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // no op
            }
        })

        view.filterCountEt.addTextChangedListener(object : TextWatcher {
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
                view.filterCount.postDelayed({
                    view.filterCount.progress = input - 1
                }, 0)

                getPresenter().setCount(input)
            }
        })
    }

    override fun setFilter(categories: List<Category>, difficulties: List<Difficulty>, filter: QuizFilter) {
        categoryAdapter.setSelectedCategory(filter.category)
        categoryAdapter.setItems(categories)

        filterAdapter.setItems(difficulties)

        val v = view!!
        val display = filter.count.toString()
        v.filterCountEt.setText(display)
        v.filterCountEt.setSelection(display.length)
        v.filterDifficultySpinner.setSelection(filterAdapter.getPositionForItem(filter.difficulty))
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        getPresenter().getFilter()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // no op
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        getPresenter().setDifficulty(filterAdapter.getItem(position))
        dropKeyboard()
    }

    override fun onClick(v: View) {
        val view = view!!
        when (v) {
            view.filterBtnStart -> getPresenter().getQuestions()
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        val category = categoryAdapter.getItem(position)
        categoryAdapter.setSelectedCategory(category)
        getPresenter().setCategory(category)
        dropKeyboard()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            getPresenter().resetFilter()
            dropKeyboard()
        }
    }

    override fun setQuestions(items: List<Question>) {
        if (items.isNotEmpty()) {
            startActivityForResult(QuizActivity.getLaunchIntent(activity!!, items), REQUEST_CODE)
        } else {
            Snackbar.make(view!!, R.string.filter_no_more, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun createPresenter(): FilterContract.Presenter =
            FilterPresenter.newInstance(applicationContext!!)

    override fun displayError(error: String) {
        snackbar = Snackbar.make(view!!, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.filter_retry) { _ ->
                    if (filterAdapter.isEmpty) getPresenter().getFilter() else getPresenter().getQuestions()
                }
        snackbar!!.show()
    }

    override fun showProgress(show: Boolean) {
        view?.filterLoading?.visibility = if (show) View.VISIBLE else View.GONE
        snackbar?.dismiss()
        snackbar = null
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