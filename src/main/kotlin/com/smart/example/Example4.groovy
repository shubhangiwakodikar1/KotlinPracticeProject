//package com.smart.example
//
//class Example4 {
//}
//
//@CompileStatic
//class GlobalHubServiceGroovy {
//
//    static transactional = false
//
//    AsyncHttpClient globalAsyncHttpClient
//    ServerConfigService serverConfigService
//    GrailsConfigService grailsConfigService
//    ShardInfoService shardInfoService
//    ManufacturedHubService manufacturedHubService
//    ManufacturedHubReadReplicaService manufacturedHubReadReplicaService
//
//    private Gson gson = new GsonBuilder().setDateFormat('yyyy-MM-dd\'T\'HH:mm:ss.SSSZ')
//    .serializeNulls()
//    .excludeFieldsWithoutExposeAnnotation()
//    .create()
//
//    static final String HUB_URL_PROPERTY = 'sharding.global.hub.url'
//
//    Observable<APIResponse<Boolean>> notifyGlobalOfClaim(Hub hub) {
//        notifyGlobalOfClaim(hub.zigbeeId, null)
//    }
//
//    Observable<APIResponse<Boolean>> notifyGlobalOfClaim(String hubId, String claimCode) {
//        String shardId = shardInfoService.shardId
//                return notifyGlobalOfClaimWithHubServiceRequest(hubId, shardId, hubClaimRequest(hubId, claimCode, shardId))
//    }
//
//    Observable<APIResponse<Boolean>> notifyGlobalOfClaim(String hubId, String shardId, String claimCode) {
//        return notifyGlobalOfClaimWithHubServiceRequest(hubId, shardId, hubClaimRequest(hubId, claimCode, shardId))
//    }
//
//    private Observable<APIResponse<Boolean>> notifyGlobalOfClaimWithHubServiceRequest(String hubId, String shardId, AsyncHttpClient.BoundRequestBuilder hubRequest) {
//        boolean hubServiceEnabled = serverConfigService.getValue(ManufacturedHubService.GLOBAL_HUB_WRITE_ENABLED)
//        Observable<APIResponse<Boolean>> claimResult = Observable.just(APIResponse.successOf(true))
//
//        if (hubServiceEnabled) {
//            Future<Response> hubFuture = hubRequest.execute()
//            Observable<APIResponse<Boolean>> hubResponseObservable = AsyncApiUtil.booleanResponseFrom2xx(hubFuture, 'global_hub_claim_put_new', [hub: hubId])
//            claimResult = hubResponseObservable
//        }
//
//        return claimResult
//    }
//
//    private AsyncHttpClient.BoundRequestBuilder hubClaimRequest(String hubId, String claimCode, String shardId) {
//        String hubUrl = grailsConfigService.getString(HUB_URL_PROPERTY)
//        return globalAsyncHttpClient.preparePost("${hubUrl}hubs/claims")
//            .addHeader("Content-Type", "application/json")
//            .setBody(gson.toJson([claimCode: claimCode, hubId: hubId, shardId: shardId]))
//        .setBodyEncoding('UTF-8')
//    }
//
//    Observable<APIResponse<Boolean>> notifyGlobalOfDelete(Hub hub) {
//        notifyGlobalOfDelete(hub.zigbeeId)
//    }
//
//    Observable<APIResponse<Boolean>> notifyGlobalOfDelete(String hubEUI) {
//        Optional<ListenableFuture<Response>> hubFutureOpt = Optional.absent()
//
//        if (serverConfigService.getValue(ManufacturedHubService.GLOBAL_HUB_WRITE_ENABLED)) {
//            String hubUrl = grailsConfigService.getString(HUB_URL_PROPERTY)
//            ListenableFuture<Response> future = globalAsyncHttpClient.prepareDelete("${hubUrl}hubs/${hubEUI}/claim").execute()
//            hubFutureOpt = Optional.of(future)
//        }
//        Observable<APIResponse<Boolean>> hubResponse
//                if (hubFutureOpt.isPresent()) {
//                    ListenableFuture<Response> responseFuture = hubFutureOpt.get()
//                    hubResponse = AsyncApiUtil.booleanResponseFromResponse(responseFuture.get(), 'global_hub_delete_new', [hub: hubEUI])
//                } else {
//                    RuntimeException notPresentException = new RuntimeException(
//                        "Failed to notify global layer of hub delete due to misconfiguration. hub-eui=$hubEUI"
//                    )
//                    APIResponse<Boolean> failedApiResponse = APIResponse.failureOf(notPresentException)
//                    hubResponse = Observable.just(failedApiResponse)
//                }
//
//        return hubResponse
//    }
//
//    Optional<ManufacturedHub> findByZigbeeId(String zigbeeId) {
//        Future<Response> future = prepareHubServiceRequest('hubs/search').addQueryParam('hubId', zigbeeId).execute()
//        return parseSearchResponse(future)
//    }
//
//    Optional<ManufacturedHub> findBySerialNumber(String serialNumber) {
//        Future<Response> future = prepareHubServiceRequest('hubs/search').addQueryParam('serialNumber', serialNumber).execute()
//        return parseSearchResponse(future)
//    }
//
//    Optional<ManufacturedHub> findByClaimCode(String claimCode) {
//        Future<Response> future = prepareHubServiceRequest('hubs/search').addQueryParam('claimCode', claimCode).execute()
//        return parseSearchResponse(future)
//    }
//
//    private AsyncHttpClient.BoundRequestBuilder prepareHubServiceRequest(String path) {
//        String hubUrl = grailsConfigService.getString(HUB_URL_PROPERTY)
//        return globalAsyncHttpClient.prepareGet(hubUrl + path)
//    }
//
//    private Optional<ManufacturedHub> parseSearchResponse(Future<Response> responseFuture) {
//        Response response = responseFuture.get()
//        Optional<ManufacturedHub> result = Optional.absent()
//        if (response.statusCode == 200) {
//            JsonObject responseObject = gson.fromJson(response.getResponseBody('UTF8'), JsonObject)
//            if (responseObject.getAsJsonArray('hubs').size() > 0) {
//                JsonElement firstHub = responseObject.getAsJsonArray('hubs').get(0)
//                GlobalManufacturedHub hub = gson.fromJson(firstHub, GlobalManufacturedHub)
//                result = Optional.of(hub.toManufacturedHub())
//            }
//        } else if (response.statusCode == 404) {
//            KVLogger.info(log, 'global_hub_not_found', [url: response.uri, statusCode: response.getStatusCode()])
//        } else {
//            KVLogger.error(log, 'global_hub_error', [url: response.uri, statusCode: response.getStatusCode()])
//        }
//
//        return result
//    }
//
//    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
//    Observable<APIResponse<Boolean>> toggleBlackList(String zigbeeId, boolean blacklist) {
//        String hubUrl = grailsConfigService.getString(HUB_URL_PROPERTY)
//        AsyncHttpClient.BoundRequestBuilder requestBuilder
//                if (blacklist) {
//                    requestBuilder = globalAsyncHttpClient.preparePut("${hubUrl}hubs/${zigbeeId}/blacklist")
//                } else {
//                    requestBuilder = globalAsyncHttpClient.prepareDelete("${hubUrl}hubs/${zigbeeId}/blacklist")
//                }
//
//        ListenableFuture<Response> future = requestBuilder.execute()
//        Observable<APIResponse<Boolean>> observable = AsyncApiUtil.booleanResponseFrom2xx(future, 'hub_blacklist', [hubId: zigbeeId, blacklist: blacklist])
//        return observable
//    }
//
//    void save(GlobalManufacturedHub globalManufacturedHub) {
//        if (globalManufacturedHub.hubId == null) {
//            throw new ValidationException(['zigbeeId is required'], false)
//        }
//        String hubUrl = grailsConfigService.getString(HUB_URL_PROPERTY)
//        AsyncHttpClient.BoundRequestBuilder requestBuilder = globalAsyncHttpClient.preparePut("${hubUrl}hubs/${globalManufacturedHub.hubId}")
//            .addHeader("Content-Type", "application/json")
//            .setBody(gson.toJson(globalManufacturedHub))
//
//        ListenableFuture<Response> future = requestBuilder.execute()
//        Response response = future.get()
//        if (response.statusCode >= 300) {
//            KVLogger.error(log, 'global_hub_write_error', [url: response.uri, statusCode: response.statusCode])
//        }
//    }
//
//    void save(ManufacturedHub manufacturedHub) {
//        if (manufacturedHub.manufacturedHubDTO == null) {
//            manufacturedHubService.storeManufacturedHubInCache(manufacturedHub)
//        }
//        save(new GlobalManufacturedHub(manufacturedHub))
//    }
//
//    Observable<APIResponse<Map>> getStToken(String username, String ecid) {
//        String hubUrl = grailsConfigService.getString(HUB_URL_PROPERTY)
//        String shardId = shardInfoService.shardId
//                Future<Response> future = globalAsyncHttpClient.preparePost("${hubUrl}hubs/stTokens")
//            .setHeader('Content-Type', 'application/json')
//            .setBody(gson.toJson([shardId: shardId, username: username, ecid: ecid]))
//        .execute()
//
//        return AsyncApiUtil.responseToObject(future, AsyncApiUtil.&responseToMap, 'get_st_token', [username: username, ecid: ecid])
//    }
//
//    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
//    void migrateHubs(boolean resolveConflicts, int globalBatchSize) {
//        migrateHubsFromObservable(manufacturedHubReadReplicaService.findAllManufacturedHubs(), resolveConflicts, globalBatchSize)
//    }
//
//    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
//    void migrateHub(String zigbeeId, boolean resolveConflicts) {
//        ManufacturedHub manufacturedHub = manufacturedHubReadReplicaService.findByZigbeeId(zigbeeId)
//        if (manufacturedHub) {
//            migrateHubsFromObservable(Observable.just(manufacturedHub), resolveConflicts, 1)
//        } else {
//            log.info("Could not migrate hub ${zigbeeId}")
//        }
//    }
//
//    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
//    void migrateHubsFromObservable(Observable<ManufacturedHub> hubObservable, boolean resolveConflicts, int globalBatchSize) {
//        String hubUrl = grailsConfigService.getString(HUB_URL_PROPERTY)
//        String shardId = shardInfoService.getShardId()
//
//        hubObservable.buffer(globalBatchSize).flatMap({ List<ManufacturedHub> hubs ->
//            Map body = [sourceShard: shardId, hubs: hubs.collect { ManufacturedHub manHub -> new GlobalManufacturedHub(manHub) }]
//            ListenableFuture<Response> responseFuture = globalAsyncHttpClient.preparePost("${hubUrl}hubs/migrate?resolveConflicts=${resolveConflicts}")
//                .addHeader("Content-Type", "application/json")
//                .setBody(gson.toJson(body))
//                .execute()
//            return Observable.from(responseFuture)
//        } as Func1).doOnError({ Throwable throwable ->
//            log.error("Error migrating hubs", throwable)
//        } as Action1)
//            .toBlocking().forEach({ Response response ->
//                log.info("Migrated hubs with status ${response.statusCode}")
//            } as Action1)
//    }
//
//    void delete(ManufacturedHub manufacturedHub) {
//        String hubUrl = grailsConfigService.getString(HUB_URL_PROPERTY)
//
//        ListenableFuture<Response> future = globalAsyncHttpClient.prepareDelete("${hubUrl}hubs/${manufacturedHub.zigbeeId}").execute()
//
//        Observable<APIResponse<Boolean>> observable = AsyncApiUtil.booleanResponseFrom2xx(future, 'hub_delete', [hubId: manufacturedHub.zigbeeId, serialNumber: manufacturedHub.serialNumber])
//        observable.toBlocking().first()
//    }
//}

import org.codehaus.groovy.runtime.EncodingGroovyMethods

public class Example4 {
    public static void main(String[] args) {
        int manufacturerId = 0x0065
        println(EncodingGroovyMethods.encodeBase64(manufacturerId))
    }
}
