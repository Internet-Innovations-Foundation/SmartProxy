package org.iif.smart_demo.network

import org.iif.smart_demo.domain.MediaResponse
import retrofit2.http.GET

interface MediaApi {
    @GET("graph-api/en/livestream/english")
    suspend fun getSmartProxyMedia(): MediaResponse
}