package me.ameriod.trivia.ui.quiz.question

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatRadioButton
import me.ameriod.trivia.R

class QuestionRadioButton : AppCompatRadioButton {

    constructor(context: Context) : super(context) {
        buttonDrawable = null
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium)
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        if (checked) {
            setTextColor(Color.WHITE)
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
        } else {
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.TRANSPARENT)
        }
    }
}