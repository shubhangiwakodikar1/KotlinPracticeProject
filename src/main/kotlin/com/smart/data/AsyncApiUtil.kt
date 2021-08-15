package com.smart.data

import com.amazonaws.util.json.Jackson
import com.fasterxml.jackson.core.type.TypeReference
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder
import com.ning.http.client.ListenableFuture
import com.ning.http.client.Response
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import io.reactivex.rxjava3.functions.Function

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import kotlin.collections.Map as Map

class AsyncApiUtil {
    companion object {
        val log: Log = LogFactory.getLog("AsyncApiUtil")

        val returnError: Function<Throwable, APIResponse<Boolean>> =
            object : Function<Throwable, APIResponse<Boolean>> {
                @Throws(Exception::class)
                override fun apply(throwable: Throwable): APIResponse<Boolean> {
                    return APIResponse.statusFailure<Boolean>(500)
                }
            }

        fun booleanResponseFrom2xx(
            future: ListenableFuture<Response>,
            logPrefix: String,
            params: Map<String, String>
        ): Observable<APIResponse<Boolean>> {
            return booleanResponseFromObservable(Observable.fromFuture(future), logPrefix, params)
        }

        fun booleanResponseFromObservable(
            observable: @NonNull Observable<Response>,
            logPrefix: String,
            params: Map<String, String>
        ): Observable<APIResponse<Boolean>> {
            return observable.map(object : Function<Response, APIResponse<Boolean>> {
                override fun apply(response: Response): APIResponse<Boolean> {
                    if (response.statusCode < 300) {
                        return APIResponse.successOf(true)
                    } else {
                        return APIResponse.statusFailure(response.statusCode)
                    }
                }
            }).onErrorReturn(object : Function<Throwable, APIResponse<Boolean>> {
                override fun apply(t: Throwable): APIResponse<Boolean> {
                    return APIResponse.failureOf(t)
                }
            })
        }

        fun booleanResponseFromResponse(
            resp: Response,
            logPrefix: String,
            params: Map<String, String>
        ): Observable<APIResponse<Boolean>> {
            return booleanResponseFromObservable(Observable.just(resp), logPrefix, params)
        }

        fun booleanResponseWithRetries(
            future: Future<Response>,
            logPrefix: String,
            params: Map<String, Int>,
            retries: Int,
            builder: BoundRequestBuilder,
            retryMillis: Long
        ): Observable<APIResponse<Boolean>> {
            return booleanResponseWithRetries(
                Observable.fromFuture(future),
                logPrefix,
                params,
                retries,
                builder,
                retryMillis
            )
        }

        fun booleanResponseWithRetries(
            future: Future<Response>,
            logPrefix: String,
            params: Map<String, Int>,
            retries: Int,
            builder: BoundRequestBuilder
        ): Observable<APIResponse<Boolean>> {
            return booleanResponseWithRetries(Observable.fromFuture(future), logPrefix, params, retries, builder)
        }

        fun booleanResponseWithRetries(
            observable: Observable<Response>,
            logPrefix: String,
            params: Map<String, Int>,
            maxRetries: Int,
            builder: BoundRequestBuilder,
            retryDelay: Long = 1000
        ): Observable<APIResponse<Boolean>> {
            return observable.map { response ->
                if (response.statusCode < 300) {
                    return@map APIResponse.successOf(true)
                } else {
                    if (response.statusCode >= 400 && response.statusCode < 500) {
                        return@map APIResponse.statusFailure(response.statusCode)
                    }
                    throw RuntimeException()
                }
            }.delay(
                retryDelay,
                TimeUnit.MILLISECONDS
            ).retry { count, throwable ->
                builder.execute()
                return@retry count < maxRetries
            }.onErrorReturn(returnError)
        }

        fun <T> responseToObject(
            resp: Future<Response>,
            closure: (Response) -> T,
            logPrefix: String,
            params: Map<String, Int>
        ): Observable<APIResponse<T>> {
            return Observable.fromFuture(resp).map { response ->
                if (response.statusCode < 300) {
                    APIResponse.successOf(closure(response))
                } else {
                    APIResponse.statusFailure(response.statusCode)
                }
            }.onErrorReturn { throwable ->
                APIResponse.failureOf<T>(throwable)
            }
        }

        fun <T> responseToObject(
         resp: Future<Response>,
         successOf: (Response) -> T,
         statusFailure: (Response) -> T,
         logPrefix: String,
         params: Map<String, Int>
        ): Observable<APIResponse<T>>
        {
            return Observable.fromFuture(resp).map { response ->
                if (response.statusCode < 300) {
                    APIResponse.successOf(successOf(response), response.statusCode)
                } else {
                    APIResponse.statusFailure(statusFailure(response), response.statusCode)
                }
            }.onErrorReturn { throwable ->
                APIResponse.failureOf(throwable)
            }
        }

        fun responseToMap(response: Response): Map<String, Int> {
            val respBody: String = response.getResponseBody("UTF8")
            return Jackson.getObjectMapper().readValue(respBody, object:TypeReference<Map<String, Int>>() {})
        }

        fun responseToList(response: Response): List<String> {
            val respBody: String = response.getResponseBody ("UTF8")
            return Jackson.getObjectMapper().readValue(respBody, object:TypeReference<List<String>>() {})
        }
    }
}
