package me.thenightmancodeth.classi.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by joe on 4/5/17.
 */
data class Class(val id: Int, val name: String, val prof: String, val location: String, val date: String, val time: String) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
        dest?.writeString(prof)
        dest?.writeString(location)
        dest?.writeString(date)
        dest?.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Class> = object : Parcelable.Creator<Class> {
            override fun createFromParcel(parcelIn: Parcel): Class {
                return Class(parcelIn.readInt(), parcelIn.readString(), parcelIn.readString(),
                        parcelIn.readString(), parcelIn.readString(), parcelIn.readString())
            }

            override fun newArray(size: Int): Array<Class> {
                return arrayOf()
            }
        }
    }
}