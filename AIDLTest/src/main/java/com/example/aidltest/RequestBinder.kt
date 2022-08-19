package com.example.aidltest

import android.os.Parcel
import android.os.Parcelable


class RequestBinder() : Parcelable {

    val clientAidlInterface = object : ClientAidlInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            TODO("Not yet implemented")
        }

        override fun request(): Int {
            return 111111
        }

        override fun getFileLiveDatas(): String {
            return "32s2sw223w"
        }

    }

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStrongBinder(clientAidlInterface)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RequestBinder> {
        override fun createFromParcel(parcel: Parcel): RequestBinder {
            return RequestBinder(parcel)
        }

        override fun newArray(size: Int): Array<RequestBinder?> {
            return arrayOfNulls(size)
        }
    }

}