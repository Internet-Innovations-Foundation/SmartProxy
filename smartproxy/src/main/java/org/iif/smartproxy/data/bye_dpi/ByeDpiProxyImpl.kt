package org.iif.smartproxy.data.bye_dpi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.iif.smartproxy.domain.AppProxy
import org.iif.smartproxy.domain.ConnectionStatus
import org.iif.smartproxy.domain.ProxyConfig
import org.iif.smartproxy.utils.ByeDpiHelper

/**
 * ByeDpi proxy implementation.
 * This class is responsible for managing the ByeDpi proxy connection.
 * @param configImpl - ByeDpi configuration implementation.
 */
class ByeDpiProxyImpl(private val configImpl: ByeDpiConfigImpl): AppProxy {
    private val _byeDpiHelper = ByeDpiHelper()
    private var _connectionStatus = ConnectionStatus.DISCONNECTED
    private var _byeDpiJob: Job? = null

    /**
     * Start the ByeDpi proxy.
     * This method initializes the ByeDpi helper and starts the proxy connection.
     */
    override suspend fun start() {
        _byeDpiJob = CoroutineScope(Dispatchers.IO).launch {
            _byeDpiHelper.start(configImpl.getConfig())
        }
        _connectionStatus = ConnectionStatus.CONNECTED
    }

    /**
     * Stop the ByeDpi proxy.
     * This method stops the ByeDpi helper and cancels the job.
     */
    override suspend fun stop() {
        _byeDpiHelper.stop()
        _byeDpiJob?.cancel()
        _byeDpiJob = null
        _connectionStatus = ConnectionStatus.DISCONNECTED
    }

    /**
     * Update the ByeDpi proxy configuration.
     * This method stops the current proxy and starts it again with the new configuration.
     * @param config - new ByeDpi configuration.
     */
    override suspend fun updateConfig(config: ProxyConfig<*>) {
        if (config !is ByeDpiConfigImpl) return
        _byeDpiHelper.run {
            stop()
            start(config.getConfig())
        }
    }

    /**
     * Get the proxy host.
     * @return the host of the ByeDpi proxy.
     */
    override fun getHost(): String? = configImpl.host

    /**
     * Get the proxy port.
     * @return the port of the ByeDpi proxy.
     */
    override fun getPort(): Int? = configImpl.port

    /**
     * Get the connection status of the ByeDpi proxy.
     * @return the current connection status.
     */
    override fun getConnectionStatus() = _connectionStatus

    /**
     * Get the proxy type.
     * @return the type of the proxy, which is SOCKS in this case.
     */
    override fun getProxyType() = java.net.Proxy.Type.SOCKS
}