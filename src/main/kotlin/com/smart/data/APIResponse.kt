package com.smart.data

import java.lang.IllegalStateException

class APIResponse<T>(val _value: T?, val exception: Throwable?, val statusCode: Int?, val success: Boolean) {

    companion object {
        fun <T> successOf(value: T): APIResponse<T> {
            return APIResponse<T>(value, null, null, true)
        }

        fun <T> successOf(value: T, statusCode: Int): APIResponse<T> {
            return APIResponse<T>(value, null, statusCode, true)
        }

        fun <T> failureOf(t: Throwable): APIResponse<T> {
            return APIResponse<T>(null, t, null, false)
        }

        fun <T> statusFailure(statusCode: Int): APIResponse<T> {
            return APIResponse<T>(null, null, statusCode, false)
        }

        fun <T> statusFailure(value: T, statusCode: Int): APIResponse<T> {
            return APIResponse<T>(value, null, statusCode, false)
        }
    }

    val failure: Boolean = !success

    val value: T
        get() = _value ?: throwOnFailure()

    fun throwOnFailure(): Nothing {
        exception?.let { throw it }
        statusCode?.let {
            if (failure) {
                throw StatusCodeException(statusCode)
            }
        }
        throw IllegalStateException("Unable to find exception")
    }
}

class StatusCodeException(val statusCode: Int) : Exception("Invalid http status code returned: ${statusCode}")
