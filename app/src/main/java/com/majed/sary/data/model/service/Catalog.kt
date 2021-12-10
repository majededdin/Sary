package com.majed.sary.data.model.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.majed.sary.data.model.enum.DataType
import com.majed.sary.utils.extentions.getListModelFromJSON
import org.json.JSONArray

data class Catalog(
    val id: Int,
    val title: String,
    private val data: Any,
    private val data_type: String,
    val show_title: Boolean,
    val ui_type: String,
    val row_count: Int
) {

    fun getSmartItemList(): ArrayList<ItemSmart> =
        JSONArray(Gson().toJson(data)).getListModelFromJSON(object :
            TypeToken<ArrayList<ItemSmart>>() {}.type)

    fun getBannerItemList(): ArrayList<ItemBanner> =
        JSONArray(Gson().toJson(data)).getListModelFromJSON(object :
            TypeToken<ArrayList<ItemBanner>>() {}.type)

    fun getGroupItemList(): ArrayList<ItemGroup> =
        JSONArray(Gson().toJson(data)).getListModelFromJSON(object :
            TypeToken<ArrayList<ItemGroup>>() {}.type)

    fun getDataType() = when (data_type) {
        DataType.SMART.type -> DataType.SMART

        DataType.BANNER.type -> DataType.BANNER

        else -> DataType.GROUP
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Catalog

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "Catalog(id=$id, title='$title', data=$data, data_type='$data_type', show_title=$show_title, ui_type='$ui_type', row_count=$row_count)"
    }
}