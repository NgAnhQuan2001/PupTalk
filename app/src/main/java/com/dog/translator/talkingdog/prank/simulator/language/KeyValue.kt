package com.dog.translator.talkingdog.prank.simulator.language

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeyValue(
    var key: String,
    var value: String,
) : Parcelable {
    constructor() : this("", "") {

    }

    override fun toString(): String {
        return value
    }

}