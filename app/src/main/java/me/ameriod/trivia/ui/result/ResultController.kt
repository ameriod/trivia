package me.ameriod.trivia.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.controller_result.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.mvvm.MvvmController
import me.ameriod.trivia.mvvm.viewModel
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter

class ResultController(args: Bundle) : MvvmController(args) {

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TriviaBaseAdapter<TriviaAdapterItem>(activity!!)
    }
    private val showDone = args.getBoolean(SHOW_DONE, true)
    private val viewModel: ResultViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.controller_result, container, false)
                    .apply {
                        resultBtnDone.setOnClickListener { _ -> activity?.finish() }
                        resultRecycler.layoutManager = LinearLayoutManager(context)
                        resultRecycler.adapter = adapter
                        resultRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

                        resultBtnDone.visibility = if (showDone) View.VISIBLE else View.GONE
                        resultRecycler.setPadding(0, 0, 0, if (showDone) context.resources.getDimensionPixelOffset(R.dimen.recycler_button_bottom_padding) else 0)
                    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        viewModel.stateLiveData.observe(this, Observer {
            setState(it)
        })

        viewModel.getResult(args.getLong(RESULT_ID))
    }

    private fun setState(state: ResultViewModel.State) {
        when (state) {
            is ResultViewModel.State.Loading -> {
                view?.apply {
                    resultLoading.isVisible = state.show
                    resultRecycler.isVisible = !state.show
                }
            }
            is ResultViewModel.State.Result -> adapter.setItems(state.items)
        }
    }
    
    companion object {
        private const val RESULT_ID = "result_id"
        private const val SHOW_DONE = "show_done"

        @JvmStatic
        fun newInstance(resultId: Long, showDone: Boolean = true): ResultController {
            val args = Bundle()
            args.putLong(RESULT_ID, resultId)
            args.putBoolean(SHOW_DONE, showDone)
            return ResultController(args)
        }
    }
}