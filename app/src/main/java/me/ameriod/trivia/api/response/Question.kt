package me.ameriod.trivia.api.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Question(@Expose
                    @SerializedName("category")
                    private val category: String,
                    @Expose
                    @SerializedName("type")
                    private val apiType: String,
                    @Expose
                    @SerializedName("difficulty")
                    private val difficulty: String,
                    @Expose
                    @SerializedName("question")
                    val question: String,
                    @Expose
                    @SerializedName("correct_answer")
                    private val correctAnswer: String,
                    @Expose
                    @SerializedName("incorrect_answers")
                    private val incorrectAnswers: List<String>) : Parcelable {

    /**
     * Gets the answers in a randomized order
     */
    val answers: List<String> by lazy {
        val choices = mutableListOf<String>()
        choices.add(correctAnswer)
        choices.addAll(incorrectAnswers)
        choices.shuffle()
        choices
    }

    /**
     * [Type]
     */
    val type: Type = Type.valueOf(String(apiType.toCharArray()
            .map { char ->
                char.toUpperCase()
            }
            .toCharArray()))

    /**
     * Tells if the selected answer is correct
     */
    fun isCorrect(answer: String) = answer == correctAnswer

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(category)
        writeString(apiType)
        writeString(difficulty)
        writeString(question)
        writeString(correctAnswer)
        writeStringList(incorrectAnswers)
    }

    enum class Type {
        MULTIPLE, BOOLEAN
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Question> = object : Parcelable.Creator<Question> {
            override fun createFromParcel(source: Parcel): Question = Question(source)
            override fun newArray(size: Int): Array<Question?> = arrayOfNulls(size)
        }
    }
}