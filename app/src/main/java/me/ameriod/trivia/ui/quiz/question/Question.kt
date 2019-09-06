package me.ameriod.trivia.ui.quiz.question

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(val text: String,
                    val answers: List<Answer>) : Parcelable