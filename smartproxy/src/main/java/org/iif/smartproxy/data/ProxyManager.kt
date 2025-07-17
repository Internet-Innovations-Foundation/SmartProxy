package org.iif.smartproxy.data

import org.iif.smartproxy.domain.AppProxy
import org.iif.smartproxy.domain.ProxyConfig
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * Proxy manager. This class is responsible for managing proxy.
 * It provides methods to start, stop, update config and get proxy.
 * @param _proxy - app proxy.
 */
class ProxyManager(private var _proxy: AppProxy) {

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
     * Update current proxy config.
     * @param config - new config.
     */
    suspend fun updateConfig(config: ProxyConfig<*>) {
        _proxy.updateConfig(config)
    }

    /**
     * Change proxy. This method stops the current proxy, changes it to the new one and starts it again.
     * @param appProxy - new app proxy.
     * @param autoStart - if true, proxy will be started after changing.
     */
    suspend fun changeProxy(appProxy: AppProxy, autoStart: Boolean = true) {
        stop()
        _proxy = appProxy
        if (autoStart) start()
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
        return Proxy(_proxy.getProxyType(), InetSocketAddress(getHost(), getPort()!!))
    }
}