package com.jordansilva.map4.util

sealed class UseCaseResult<out T> {
    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this !is Success

    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Failure<out T>(val error: ErrorResult) : UseCaseResult<T>() {
        constructor(message: String? = null, exception: Throwable? = null) : this(ErrorResult(message, exception))
    }
}

open class ErrorResult(val message: String? = null, val cause: Throwable? = null)

inline fun <T> UseCaseResult<T>.onFailure(action: (error: ErrorResult) -> Unit): UseCaseResult<T> {
    if (this is UseCaseResult.Failure) {
        action(error)
    }
    return this
}

inline fun <T> UseCaseResult<T>.onSuccess(action: (value: T) -> Unit): UseCaseResult<T> {
    if (this is UseCaseResult.Success) {
        action(data)
    }
    return this
}