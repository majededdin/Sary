package com.majed.sary.data.remote

import com.majed.sary.data.model.service.Meta
import org.json.JSONObject

data class Resource<M>(
    val status: Status,
    val parentJSON: JSONObject?,
    val code: String,
    val message: String?,
    var model: M?,
    var meta: Meta = Meta(),
    var listOfModel: MutableList<M> = ArrayList()
) {

    constructor() : this(Status.IDEL, null, "0", null, null, Meta(), arrayListOf())

    enum class Status {
        IDEL,
        OnLoading,
        OnSuccess,
        OnAuth,
        OnNotFound,
        OnError,
        OnBackEndError,
        OnHttpException,
        OnConnectException,
        OnTimeoutException,
        OnUnknownHost,
        OnFailure
    }

    companion object {
        fun <M> loading(): Resource<M> =
            Resource(Status.OnLoading, null, "0", null, null, Meta(), arrayListOf())

        fun <M> success(): Resource<M> =
            Resource(Status.OnSuccess, null, "0", null, null, Meta(), arrayListOf())

        fun <M> success(
            parentJSON: JSONObject, model: M, code: String, message: String?
        ): Resource<M> = Resource(
            Status.OnSuccess, parentJSON, code, message, model, Meta(), arrayListOf()
        )

        fun <M> success(listOfModel: MutableList<M>): Resource<M> = Resource(
            Status.OnSuccess, null, "0", null, null, Meta(), listOfModel
        )

        fun <M> success(
            parentJSON: JSONObject, listOfModel: MutableList<M>, code: String, message: String?
        ): Resource<M> = Resource(
            Status.OnSuccess, parentJSON, code, message, null, Meta(), listOfModel
        )

        fun <M> success(
            parentJSON: JSONObject, listOfModel: MutableList<M>, meta: Meta, message: String?
        ): Resource<M> {
            return Resource(Status.OnSuccess, parentJSON, "0", message, null, meta, listOfModel)
        }

        fun <M> success(parentJSON: JSONObject?, code: String, message: String?): Resource<M> =
            Resource(Status.OnSuccess, parentJSON, code, message, null, Meta(), arrayListOf())

        fun <M> onAuth(parentJSON: JSONObject?, message: String?): Resource<M> =
            Resource(Status.OnAuth, parentJSON, "0", message, null, Meta(), arrayListOf())

        fun <M> onNotFound(message: String?): Resource<M> =
            Resource(Status.OnNotFound, null, "0", message, null, Meta(), arrayListOf())

        fun <M> onErrorBody(parentJSON: JSONObject?, code: String, message: String?): Resource<M> =
            Resource(Status.OnError, parentJSON, code, message, null, Meta(), arrayListOf())

        fun <M> onRetryLimitExceeded(
            parentJSON: JSONObject?, code: String, message: String?
        ): Resource<M> = Resource(
            Status.OnError, parentJSON, code, message, null, Meta(), arrayListOf()
        )

        fun <M> onBackEndError(code: String, message: String?): Resource<M> =
            Resource(Status.OnBackEndError, null, code, message, null, Meta(), arrayListOf())

        fun <M> onHttpException(message: String?): Resource<M> =
            Resource(Status.OnHttpException, null, "0", message, null, Meta(), arrayListOf())

        fun <M> onConnectException(message: String?): Resource<M> =
            Resource(Status.OnConnectException, null, "0", message, null, Meta(), arrayListOf())

        fun <M> onTimeoutException(message: String?): Resource<M> =
            Resource(Status.OnTimeoutException, null, "0", message, null, Meta(), arrayListOf())

        fun <M> onUnknownHost(message: String?): Resource<M> =
            Resource(Status.OnUnknownHost, null, "0", message, null, Meta(), arrayListOf())

        fun <M> onFailure(message: String?): Resource<M> =
            Resource(Status.OnFailure, null, "0", message, null, Meta(), arrayListOf())
    }

    override fun toString(): String {
        return "Resource(status=$status, parentJSON=$parentJSON, code=$code, message=$message, model=$model, meta=$meta, listOfModel=$listOfModel)"
    }
}