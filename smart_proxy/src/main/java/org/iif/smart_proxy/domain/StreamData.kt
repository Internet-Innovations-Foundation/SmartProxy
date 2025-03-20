package org.iif.smart_proxy.domain

data class StreamData(
    val url: String?,
    val type: StreamType?
) {
    fun isValid() = !url.isNullOrEmpty() && type != null
}

enum class StreamType {
    HLS, AUDIO
}