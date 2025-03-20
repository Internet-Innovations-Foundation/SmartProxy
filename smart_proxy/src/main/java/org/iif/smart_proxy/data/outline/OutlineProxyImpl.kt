package org.iif.smart_proxy.data.outline

import org.iif.smart_proxy.domain.AppProxy
import org.iif.smart_proxy.domain.ConnectionStatus
import org.iif.smart_proxy.domain.ProxyConfig
import mobileproxy.Mobileproxy
import mobileproxy.Proxy
import mobileproxy.StreamDialer

/**
 * Outline proxy implementation.
 * @param config - outline config.
 */
class OutlineProxyImpl(config: OutlineConfigImpl) : AppProxy {

    private var _config: OutlineConfigImpl = config
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
     * Run new smart stream dialer.
     */
    private fun runSmartStreamDialer() {
        _streamDialer = Mobileproxy.newSmartStreamDialer(
            Mobileproxy.newListFromLines(_config.getTargetHost()),
            _config.getConfig(),
            Mobileproxy.newStderrLogWriter()
        )
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
        if (config !is OutlineConfigImpl) return
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