package org.iif.smartproxy.utils

import android.webkit.WebView
import androidx.webkit.ProxyConfig
import androidx.webkit.ProxyController

/**
 * Start proxy for WebView.
 * Use this extension method if you need to proxy data in a WebView.
 * @param proxyHost - proxy host.
 * @param proxyPort - proxy port.
 * @param onProxyConnected - callback when proxy connected.
 */
fun WebView.start(proxyHost: String?, proxyPort: Int?, onProxyConnected: (WebView) -> Unit) {
    val config = ProxyConfig.Builder()
        .addProxyRule("$proxyHost:$proxyPort")
        .addDirect()
        .build()

    ProxyController.getInstance()
        .setProxyOverride(
            config,
            { onProxyConnected(this) },
            { onProxyConnected(this) }
        )
}

/**
 * Stop proxy for WebView.
 * @param onProxyDisconnected - callback when proxy disconnected.
 */
fun WebView.stop(onProxyDisconnected: (WebView) -> Unit) {
    ProxyController.getInstance().clearProxyOverride(
        { onProxyDisconnected(this) },
        { onProxyDisconnected(this) }
    )
}