package org.iif.smartproxy.data.outline.smart

import android.util.Log
import org.iif.smartproxy.domain.AppProxy
import org.iif.smartproxy.domain.ConnectionStatus
import org.iif.smartproxy.domain.ProxyConfig
import mobileproxy.Mobileproxy
import mobileproxy.Proxy
import mobileproxy.StreamDialer
import mobileproxy.SmartDialerOptions
import mobileproxy.StrategyCache

// Pseudo Kotlin cache interface for strategy cache
// In-Memory Cache Implementation
class InMemoryStrategyCache : StrategyCache {
    private val cache = mutableMapOf<String, String>()
    private val lock = Any()

    override fun get(key: String): String {
        synchronized(lock) {
            return cache[key] ?: ""
        }
    }

    override fun put(key: String, value: String) {
        synchronized(lock) {
            if (value.isEmpty()) {
                cache.remove(key)
            } else {
                cache[key] = value
            }
        }
    }
}

/**
 * Outline proxy implementation for smart dialer.
 * SmartDialer uses the provided configuration to select the appropriate connection strategy.
 * @param config - outline config.
 */
class SmartOutlineProxyImpl(config: SmartOutlineConfigImpl) : AppProxy {

    private var _config: SmartOutlineConfigImpl = config
    private val _localHost = "localhost:0"
    private var _outlineProxy: Proxy? = null
    private var _connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED
    private var _streamDialer: StreamDialer? = null

    private var _streamDialerOptions: SmartDialerOptions? = null

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
        // Starting StreamDialer via newSmartDialerOptions
        val cache = InMemoryStrategyCache()
        _streamDialerOptions = Mobileproxy.newSmartDialerOptions(Mobileproxy.newListFromLines(_config.getTargetHost()), _config.getConfig())
        _streamDialerOptions?.setStrategyCache(cache)
        _streamDialer = _streamDialerOptions?.newStreamDialer()
        Log.d("OUTLINE", _streamDialerOptions?.getTLSTransportStrategy().toString()) // Winning TLS strategy
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
        if (config !is SmartOutlineConfigImpl) return
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