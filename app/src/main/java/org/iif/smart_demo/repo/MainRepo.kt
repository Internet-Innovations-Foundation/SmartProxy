package org.iif.smart_demo.repo

import org.iif.smart_proxy.data.ProxyManager
import org.iif.smart_demo.network.MediaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val _api: MediaApi,
    private val _proxyManager: ProxyManager
) {

    suspend fun getSmartProxyMedia() = withContext(Dispatchers.IO) {
        _api.getSmartProxyMedia().data?.livestreamChannels?.first()
    }

    fun getProxy() = _proxyManager.getProxy()

    suspend fun stopProxy() {
        _proxyManager.stop()
    }

    suspend fun startProxy() {
        _proxyManager.start()
    }

}