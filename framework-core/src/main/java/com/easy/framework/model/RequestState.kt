package com.easy.framework.model

sealed class RequestState<out T> {
    object Loading : RequestState<Nothing>()
    data class Success<out T>(val data: T) : RequestState<T>()
    data class Error(
        val message: String = "",
        val error: Throwable? = null
    ) : RequestState<Nothing>()
}

sealed class ResultStatus<out T> {
    object Loading : ResultStatus<Nothing>()
    data class Success<out T>(val data: T) : ResultStatus<T>()
    data class Error(
        val message: String = "",
        val error: Throwable? = null
    ) : ResultStatus<Nothing>()
}
