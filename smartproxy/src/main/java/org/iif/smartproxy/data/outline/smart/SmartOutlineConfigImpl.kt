package org.iif.smartproxy.data.outline.smart

import android.net.Uri
import com.google.gson.Gson
import org.iif.smartproxy.domain.ProxyConfig
import org.iif.smartproxy.utils.Utils

/**
 * Outline config implementation for smart dialer.
 * @param targetUrl - stream url.
 * @param dns - dns list.
 * @param tls - tls list.
 */
data class SmartOutlineConfigImpl(
    @Transient
    val targetUrl: String,
    val dns: List<*>?,
    val tls: List<*>?
) : ProxyConfig<String>() {

    /**
     * Get target host from stream url.
     * @return host
     */
    fun getTargetHost(): String {
        var hostString = ""
        val hosts = targetUrl.split("\n")
        hosts.forEachIndexed { index, s ->
            hostString += Uri.parse(s).host
            if (index != hosts.size - 1) hostString += "\n"
        }
        return hostString
    }

    /**
     * Get config as json string.
     * @return json string.
     */
    override fun getConfig() = Gson().toJson(this)

    companion object {

        /**
         * Create default outline config.
         * @param defaultUrl - default stream url.
         * @return default config.
         */
        fun default(defaultUrl: String): SmartOutlineConfigImpl {
            return Gson()
                .fromJson(Utils.OUTLINE_DEFAULT_CONFIG, SmartOutlineConfigImpl::class.java)
                .copy(targetUrl = defaultUrl)
        }
    }
}