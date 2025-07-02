package org.iif.smartproxy.data.outline.dafault

import org.iif.smartproxy.domain.ProxyConfig

/**
 * Outline config implementation for simple connection strategy.
 * @param config - outline strategy config.
 */
data class DefaultOutlineConfigImpl(private val _config: String) : ProxyConfig<String>() {
    override fun getConfig() = _config
}