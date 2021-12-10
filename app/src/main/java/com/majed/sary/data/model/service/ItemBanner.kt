package com.majed.sary.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemBanner(
    val image: String,
    val deep_link: String
) : Parcelable {
    override fun toString(): String {
        return "ItemBanner(image='$image', deep_link='$deep_link')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemBanner

        if (image != other.image) return false
        if (deep_link != other.deep_link) return false

        return true
    }

    override fun hashCode(): Int {
        var result = image.hashCode()
        result = 31 * result + deep_link.hashCode()
        return result
    }
}