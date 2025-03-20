package org.iif.smart_proxy.data

import org.iif.smart_proxy.domain.AppProxy
import org.iif.smart_proxy.domain.ProxyConfig
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * Proxy manager. This class is responsible for managing proxy.
 * It provides methods to start, stop, update config and get proxy.
 * @param _proxy - app proxy.
 */
class ProxyManager(private val _proxy: AppProxy) {

    /**
     * Start proxy.
     */
    suspend fun start() {
        _proxy.start()
    }

    /**
     * Stop proxy.
     */
    suspend fun stop() {
        _proxy.stop()
    }

    /**
     * Update proxy config.
     * @param config - new config.
     */
    suspend fun updateConfig(config: ProxyConfig<*>) {
        _proxy.updateConfig(config)
    }

    /**
     * Get proxy host.
     * @return proxy host.
     */
    fun getHost() = _proxy.getHost()

    /**
     * Get proxy port.
     * @return proxy port.
     */
    fun getPort() = _proxy.getPort()

    /**
     * Get proxy connection status.
     * @return connection status.
     */
    fun getConnectionStatus() = _proxy.getConnectionStatus()

    /**
     * Get net.Proxy for use with network layout.
     * @return proxy.
     */
    fun getProxy(): Proxy {
        if (getHost().isNullOrEmpty() || getPort() == null) {
            throw IllegalStateException("Proxy is not initialized")
        }
        return Proxy(Proxy.Type.HTTP, InetSocketAddress(getHost(), getPort()!!))
    }
}