package me.thenightmancodeth.classi.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by joe on 4/5/17.
 */
data class User(val id: Int, val uname: String, val pass: String) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(uname)
        dest?.writeString(pass)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(parcelIn: Parcel): User {
                return User(parcelIn.readInt(), parcelIn.readString(), parcelIn.readString())
            }

            override fun newArray(size: Int): Array<User> {
                return arrayOf()
            }
        }
    }
}