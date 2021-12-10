package com.majed.sary.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meta(
    var current_page: Int,
    var from: Int,
    var last_page: Int,
    var path: String,
    var per_page: Int,
    var to: Int,
    var total: Int
) : Parcelable {

    constructor() : this(1, 1, 1, "", 0, 0, 0)

    fun isNotEmpty(): Boolean {
        return path.isNotEmpty() && per_page != -1 && to != -1 && total != -1
    }

    override fun toString(): String {
        return "Meta(current_page=$current_page, from=$from, last_page=$last_page, path='$path', per_page=$per_page, to=$to, total=$total)"
    }

}