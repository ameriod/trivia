package me.ameriod.trivia.ui.history

import io.reactivex.Observable
import me.ameriod.lib.mvp.Mvp
import me.ameriod.trivia.api.db.Result

class HistoryContract {
    interface View : Mvp.View {
        fun setHistory(items: List<Result>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun getHistory()
    }

    interface Interactor {
        fun getHistory(): Observable<List<Result>>
    }
}