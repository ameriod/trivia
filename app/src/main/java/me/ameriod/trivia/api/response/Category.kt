package me.ameriod.trivia.api.response

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import me.ameriod.trivia.R

data class Category(@Expose
                    @SerializedName("name")
                    val name: String,
                    @Expose
                    @SerializedName("id")
                    val id: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(id)
    }

    companion object {

        @JvmStatic
        fun createAll(context: Context) : Category  {
            return Category(context.getString(R.string.filter_category_all), null)
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = Category(source)
            override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
        }
    }
}