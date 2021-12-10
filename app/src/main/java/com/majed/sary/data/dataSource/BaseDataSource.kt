package com.majed.sary.data.dataSource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import com.majed.sary.data.model.service.Meta
import com.majed.sary.data.remote.Resource
import com.majed.sary.utils.extentions.toLog
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val CODE = "code"
private const val MESSAGE = "message"
private const val META = "meta"
private const val ERRORS = "errors"
private const val LIST = "list"

abstract class BaseDataSource {

    var meta: Meta = Meta()
    private lateinit var modelAsJSONArray: JSONArray
    private lateinit var modelAsJSONObject: JSONObject

    protected suspend fun <M> getApiResult(
        call: suspend () -> ResponseBody
    ): Resource<M> = getApiResult(call, null, null, null)

    protected suspend fun <M> getApiResult(
        call: suspend () -> ResponseBody, typeToken: Type
    ): Resource<M> = getApiResult(call, typeToken, null, null)

    protected suspend fun <M> getApiResult(
        call: suspend () -> ResponseBody, model: M
    ): Resource<M> = getApiResult(call, null, null, model)

    protected suspend fun <M> getApiResult(
        call: suspend () -> ResponseBody, typeToken: Type, jsonKey: String
    ): Resource<M> = getApiResult(call, typeToken, jsonKey, null)

    private suspend fun <M> getApiResult(
        call: suspend () -> ResponseBody, typeToken: Type?, jsonKey: String?, model: M?
    ): Resource<M> = try {
        val response = call().string()
        response.toLog()
        try {
            extractFromJSONObject(JSONObject(response), typeToken, jsonKey, model)
        } catch (exception: JSONException) {
            extractFromJSONArray(JSONArray(response), typeToken)
        }
    } catch (exception: Exception) {
        getApiCallError(exception)
    }

    //------------------------------------------ Handle Api Result ---------------------------------

    private fun <M> extractFromJSONObject(
        jsonObject: JSONObject, typeToken: Type?, jsonKey: String?, model: M?
    ): Resource<M> = when {
        typeToken != null && jsonKey != null -> {   // Used when there is model returned from api with specific jsonKey
            if (jsonObject.has(jsonKey)) {
                when {
                    jsonObject.get(jsonKey) is JSONObject -> {
                        this.modelAsJSONObject = jsonObject.getJSONObject(jsonKey)
                        Resource.success(
                            jsonObject,
                            getModelFromJSON(jsonObject.getJSONObject(jsonKey), typeToken) as M,
                            jsonObject.optString(CODE), jsonObject.optString(MESSAGE)
                        )
                    }
                    jsonObject.get(jsonKey) is JSONArray -> {
                        this.modelAsJSONArray = jsonObject.getJSONArray(jsonKey)
                        if (jsonObject.has(META)) Resource.success(
                            jsonObject,
                            getListOfModelFromJSON(jsonObject.getJSONArray(jsonKey), typeToken),
                            getMetaFromJSON(jsonObject), jsonObject.optString(MESSAGE)
                        )
                        else Resource.success(
                            jsonObject,
                            getListOfModelFromJSON(jsonObject.getJSONArray(jsonKey), typeToken),
                            jsonObject.optString(CODE), jsonObject.optString(MESSAGE)
                        )
                    }
                    else -> Resource.onFailure("JSONKey $jsonKey can not be casted...")
                }
            } else Resource.onFailure("JSONKey $jsonKey is not Exist...")
        }

        typeToken != null && jsonKey == null -> {   // Used when there is model returned from api with no key
            extractBasedOnTypeToken(jsonObject, typeToken)
        }

        model != null -> {  // Used when passing model from param not from api
            this.modelAsJSONObject = jsonObject
            Resource.success(
                jsonObject, model, jsonObject.optString(CODE), jsonObject.optString(MESSAGE)
            )
        }

        else -> {   // Used when there is no model exist
            this.modelAsJSONObject = jsonObject
            Resource.success(jsonObject, jsonObject.optString(CODE), jsonObject.optString(MESSAGE))
        }
    }

    private fun <M> extractFromJSONArray(jsonArray: JSONArray, typeToken: Type?): Resource<M> =
        when {
            typeToken != null -> extractBasedOnTypeToken(jsonArray, typeToken)

            else -> {   // Used when there is no model exist
                modelAsJSONArray = jsonArray
                Resource.onFailure("JSONArray can not be casted without typeToken. \n JSONArray: $jsonArray")
            }
        }

    private fun <M> extractBasedOnTypeToken(json: Any, typeToken: Type): Resource<M> = when {
        typeToken.toString().lowercase().contains(LIST) -> {
            this.modelAsJSONArray = json as JSONArray
            Resource.success(getListOfModelFromJSON(json, typeToken))
        }
        else -> {
            this.modelAsJSONObject = json as JSONObject
            Resource.success(
                json, getModelFromJSON(json, typeToken) as M, json.optString(CODE),
                json.optString(MESSAGE)
            )
        }
    }

    //------------------------------------------ Handle Api Error ----------------------------------

    private fun <M> getApiCallError(throwable: Throwable): Resource<M> = when (throwable) {
        is HttpException -> getHttpException(throwable)

        is ConnectException -> Resource.onConnectException(throwable.message)

        is SocketTimeoutException -> Resource.onTimeoutException(throwable.message)

        is UnknownHostException -> Resource.onUnknownHost(throwable.message)

        else -> {
            val throwableMsg = throwable.message
            ("throwableMsg: $throwableMsg").toLog()
            Resource.onFailure(throwableMsg)
        }
    }

    private fun <M> getHttpException(throwable: Throwable): Resource<M> {
        val throwableMessage = (throwable as HttpException).response()?.errorBody()?.string()
        ("ErrorCode: ${throwable.code()}, ThrowableMessage: $throwableMessage").toLog()
        val jsonObject = throwableMessage?.let { JSONObject(it) }
        return when (throwable.code()) {
            401 -> Resource.onAuth(jsonObject, jsonObject?.optString(MESSAGE) ?: throwable.message)

            404 -> Resource.onNotFound(jsonObject?.optString(MESSAGE) ?: throwable.message)

            422 -> Resource.onErrorBody(
                jsonObject?.optJSONObject(ERRORS), jsonObject?.optString(CODE) ?: "0",
                jsonObject?.optString(MESSAGE) ?: throwable.message
            )

            429 -> Resource.onRetryLimitExceeded(
                jsonObject, jsonObject?.optString(CODE) ?: "90",
                jsonObject?.optString(MESSAGE) ?: throwable.message
            )

            500 -> Resource.onBackEndError(
                jsonObject?.optString(CODE) ?: "0",
                jsonObject?.optString(MESSAGE) ?: throwableMessage ?: throwable.message
            )

            else -> Resource.onHttpException(throwableMessage ?: throwable.message)
        }
    }

    //------------------------------------------ Private Parsing Methods ---------------------------

    private fun <M> getModelFromJSON(jsonObject: JSONObject, tokenType: Type): M =
        Gson().fromJson(jsonObject.toString(), tokenType)

    private fun <M> getListOfModelFromJSON(jsonArray: JSONArray, tokenType: Type): ArrayList<M> =
        Gson().fromJson(jsonArray.toString(), tokenType)

    private fun getMetaFromJSON(parentJSON: JSONObject): Meta = Gson().fromJson(
        parentJSON.getJSONObject(META).toString(), object : TypeToken<Meta>() {}.type
    )

    //------------------------------------------ Setting Methods -----------------------------------

    fun getModelAsString(): String = modelAsJSONObject.toString()

    fun getListOfModelAsString(): String = modelAsJSONArray.toString()
}
