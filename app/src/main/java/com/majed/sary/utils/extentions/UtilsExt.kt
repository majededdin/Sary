package com.majed.sary.utils.extentions

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

fun Date.getCurrentDateTimeFormatted(pattern: String): String {
    val format = SimpleDateFormat(pattern, Locale.US)
    return format.format(this)
}

fun <M> JSONObject.getModelFromJSON(tokenType: Type): M =
    Gson().fromJson(this.toString(), tokenType)

fun <M> JSONArray.getListModelFromJSON(tokenType: Type): ArrayList<M> =
    Gson().fromJson(this.toString(), tokenType)