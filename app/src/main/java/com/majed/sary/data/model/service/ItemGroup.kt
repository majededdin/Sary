package com.majed.sary.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemGroup(
    val group_id: Int,
//    val filters: ArrayList<Filter>,
    val name: String,
    val image: String,
    val cover: String?
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemGroup

        if (group_id != other.group_id) return false

        return true
    }

    override fun hashCode(): Int {
        return group_id
    }

    override fun toString(): String {
        return "ItemGroup(group_id=$group_id, name='$name', image='$image', cover=$cover)"
    }
}