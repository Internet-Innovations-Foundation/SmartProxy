package org.iif.smart_proxy.data.outline.dafault

import mobileproxy.Mobileproxy
import mobileproxy.Proxy
import mobileproxy.StreamDialer
import org.iif.smart_proxy.domain.AppProxy
import org.iif.smart_proxy.domain.ConnectionStatus
import org.iif.smart_proxy.domain.ProxyConfig

/**
 * Outline proxy implementation for simple dialer.
 * @param config - outline config implementation.
 */
class DefaultOutlineProxyImpl(private var _config: DefaultOutlineConfigImpl) : AppProxy {

    private val _localHost = "localhost:0"
    private var _outlineProxy: Proxy? = null
    private var _connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED
    private var _streamDialer: StreamDialer? = null

    /**
     * Start outline proxy.
     */
    override suspend fun start() {
        runSmartStreamDialer()
        _outlineProxy = Mobileproxy.runProxy(_localHost, _streamDialer)
        _connectionStatus = ConnectionStatus.CONNECTED
    }

    /**
     * Run new stream dialer.
     */
    private fun runSmartStreamDialer() {
        _streamDialer = StreamDialer(_config.getConfig())
    }

    /**
     * Stop outline proxy.
     */
    override suspend fun stop() {
        _connectionStatus = ConnectionStatus.DISCONNECTED
        _outlineProxy?.stop(1)
    }

    /**
     * Update outline proxy config.After updating, proxy will be restarted.
     * @param config - new config.
     */
    override suspend fun updateConfig(config: ProxyConfig<*>) {
        if (config !is DefaultOutlineConfigImpl) return
        _config = config
        start()
    }

    /**
     * Get proxy host.
     * @return proxy host.
     */
    override fun getHost() = _outlineProxy?.host()

    /**
     * Get proxy port.
     * @return proxy port.
     */
    override fun getPort() = _outlineProxy?.port()?.toInt()

    /**
     * Get proxy connection status.
     * @return connection status.
     */
    override fun getConnectionStatus() = _connectionStatus
}