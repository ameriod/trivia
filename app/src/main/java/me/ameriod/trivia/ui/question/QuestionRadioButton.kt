package me.ameriod.trivia.ui.question

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.widget.RadioButton
import me.ameriod.trivia.R

class QuestionRadioButton : RadioButton {

    constructor(context: Context?) : super(context) {
        buttonDrawable = null
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