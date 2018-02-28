package me.ameriod.trivia.api.response

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.filter.CategoryViewHolder

data class OtCategory(@Expose
                      @SerializedName("name")
                      val name: String,
                      @Expose
                      @SerializedName("id")
                      val id: String? = null,
                      var selected: Boolean = false) : Parcelable, TriviaAdapterItem {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*> {
        return CategoryViewHolder(inflater.inflate(R.layout.category_item, parent, attachToRoot))
    }

    override fun getViewType(): Int = R.layout.category_item

    override fun getRecyclerItemId(): String = id ?: ""

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(id)
        writeInt((if (selected) 1 else 0))
    }

    companion object {
        @JvmStatic
        fun createAll(context: Context): OtCategory {
            return OtCategory(context.getString(R.string.filter_category_all), null, true)
        }

        @JvmField
        val CREATOR: Parcelable.Creator<OtCategory> = object : Parcelable.Creator<OtCategory> {
            override fun createFromParcel(source: Parcel): OtCategory = OtCategory(source)
            override fun newArray(size: Int): Array<OtCategory?> = arrayOfNulls(size)
        }
    }
}