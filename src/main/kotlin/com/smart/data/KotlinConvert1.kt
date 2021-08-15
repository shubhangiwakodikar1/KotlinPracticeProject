package com.smart.data

import com.google.common.base.Optional
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ning.http.client.AsyncHttpClient
import com.ning.http.client.ListenableFuture
import com.ning.http.client.Response
import io.reactivex.rxjava3.core.Observable
import java.util.*
import java.util.concurrent.Future

interface ServerConfigService {
    fun getValue(globalHubWriteEnabled: Any): Boolean
}

interface GrailsConfigService {
    fun getString(hubUrlProperty: String): String
}

interface ShardInfoService {
    val shardId: String
}

interface ManufacturedHubService {
    object GLOBAL_HUB_WRITE_ENABLED {
    }
}

interface ManufacturedHubReadReplicaService

interface Hub {
    val zigbeeId: String
}

class GlobalHubService(
    val globalAsyncHttpClient: AsyncHttpClient,
    val serverConfigService: ServerConfigService,
    val grailsConfigService: GrailsConfigService,
    val shardInfoService: ShardInfoService,
    val manufacturedHubService: ManufacturedHubService,
    val manufacturedHubReadReplicaService: ManufacturedHubReadReplicaService
) {
    companion object {
        val transactional = false
        const val HUB_URL_PROPERTY = "sharding.global.hub.url"
    }

    val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSSZ")
        .serializeNulls()
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    fun notifyGlobalOfClaim(hub: Hub): Observable<APIResponse<Boolean>> {
        return notifyGlobalOfClaim(hub.zigbeeId, null)
    }

    private fun notifyGlobalOfClaim(hubId: String, claimCode: String?): Observable<APIResponse<Boolean>> {
        val shardId: String = shardInfoService.shardId
        return notifyGlobalOfClaimWithHubServiceRequest(hubId, shardId, hubClaimRequest(hubId, claimCode, shardId))
    }

    private fun notifyGlobalOfClaimWithHubServiceRequest(
        hubId: String,
        shardId: String,
        hubClaimRequest: AsyncHttpClient.BoundRequestBuilder
    ): Observable<APIResponse<Boolean>> {
        val hubServiceEnabled: Boolean =
            serverConfigService.getValue(ManufacturedHubService.GLOBAL_HUB_WRITE_ENABLED)
        var claimResult: Observable<APIResponse<Boolean>> = Observable.just(APIResponse.successOf(true))

        if (hubServiceEnabled) {
            val hubFuture: com.ning.http.client.ListenableFuture<com.ning.http.client.Response> =
                hubClaimRequest.execute()
            val hubResponseObservable: Observable<APIResponse<Boolean>> = AsyncApiUtil.booleanResponseFrom2xx(
                hubFuture, "global_hub_claim_put_new", mapOf("hub" to hubId)
            )
            claimResult = hubResponseObservable
        }

        return claimResult
    }

    fun hubClaimRequest(hubId: String, claimCode: String?, shardId: String): AsyncHttpClient.BoundRequestBuilder {
        val hubUrl: String = grailsConfigService.getString(HUB_URL_PROPERTY)
        return globalAsyncHttpClient.preparePost("${hubUrl}hubs/claims")
            .addHeader("Content-Type", "application/json")
            .setBody(
                gson.toJson(
                    mapOf(
                        "claimCode" to claimCode,
                        "hubId" to hubId,
                        "shardId" to shardId
                    )
                )
            )
            .setBodyEncoding("UTF-8")
    }

    fun notifyGlobalOfDelete(hub: Hub): Observable<APIResponse<Boolean>> {
        return notifyGlobalOfDelete(hub.zigbeeId)
    }

    fun notifyGlobalOfDelete(hubEUI: String): Observable<APIResponse<Boolean>> {
        var hubFutureOpt: Optional<ListenableFuture<Response>> = Optional.absent()

        if (serverConfigService.getValue(ManufacturedHubService.GLOBAL_HUB_WRITE_ENABLED)) {
            val hubUrl: String = grailsConfigService.getString(HUB_URL_PROPERTY)
            val future: ListenableFuture<Response> =
                globalAsyncHttpClient.prepareDelete("${hubUrl}hubs/${hubEUI}/claim").execute()
            hubFutureOpt = Optional.of(future)
        }
        val hubResponse: Observable<APIResponse<Boolean>>
        if (hubFutureOpt.isPresent()) {
            val responseFuture: ListenableFuture<Response> = hubFutureOpt.get()
            hubResponse = AsyncApiUtil.booleanResponseFromResponse(
                responseFuture.get(), "global_hub_delete_new", mapOf("hub" to hubEUI)
            )
        } else {
            val notPresentException = RuntimeException(
                "Failed to notify global layer of hub delete due to misconfiguration. hub-eui=$hubEUI"
            )
            val failedApiResponse = APIResponse.failureOf<Boolean>(notPresentException)
            hubResponse = Observable.just(failedApiResponse)
        }

        return hubResponse
    }
}



