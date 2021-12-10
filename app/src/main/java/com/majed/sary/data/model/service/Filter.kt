package com.majed.sary.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filter(
    val filter_id: Int,
    val name: String
) : Parcelable {
    override fun toString(): String {
        return "Filter(filter_id=$filter_id, name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Filter

        if (filter_id != other.filter_id) return false

        return true
    }

    override fun hashCode(): Int {
        return filter_id
    }
}