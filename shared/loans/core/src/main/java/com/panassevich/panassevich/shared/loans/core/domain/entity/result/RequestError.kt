package com.panassevich.panassevich.shared.loans.core.domain.entity.result

sealed class RequestError<out T> {

    data class NetworkError<T>(val cachedContent: T?) : RequestError<T>()

    data class ServerError<T>(val cachedContent: T?) : RequestError<T>()

    object InvalidInput : RequestError<Nothing>()

    object UserAlreadyExists : RequestError<Unit>()

    object UserNotFound : RequestError<Unit>()

}
