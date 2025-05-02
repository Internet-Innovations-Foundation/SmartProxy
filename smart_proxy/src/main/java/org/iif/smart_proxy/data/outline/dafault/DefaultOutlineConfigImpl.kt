package org.iif.smart_proxy.data.outline.dafault

import org.iif.smart_proxy.domain.ProxyConfig

/**
 * Outline config implementation for simple connection strategy.
 * @param config - outline strategy config.
 */
data class DefaultOutlineConfigImpl(private val _config: String) : ProxyConfig<String>() {
    override fun getConfig() = _config
}