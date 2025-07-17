package org.iif.smartproxy.data.bye_dpi

import org.iif.smartproxy.domain.ProxyConfig

/**
 * ByeDpi config implementation.
 * This class is responsible for providing ByeDpi configuration.
 * @param host - the host to bind the proxy to.
 * @param port - the port to bind the proxy to.
 * @param config - ByeDpi configuration string.
 *                 Example: "--disorder 1 --auto=torst --tlsrec 1+s"
 */
class ByeDpiConfigImpl(
    val host: String = "0.0.0.0",
    val port: Int = 1080,
    private val config: String
) : ProxyConfig<Array<String>>() {
    override fun getConfig(): Array<String> {
        val regex = Regex("(?=\\s-[^-\\s]|\\s--[^\\s])")
        val params = mutableSetOf("ciadpi")
        config
            .split(regex)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .let { params.addAll(it) }
        return params.toTypedArray()
    }
}