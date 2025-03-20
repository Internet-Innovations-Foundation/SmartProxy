package org.iif.smart_proxy.utils

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import org.iif.smart_proxy.domain.StreamData
import org.iif.smart_proxy.domain.StreamType
import okhttp3.OkHttpClient
import java.net.Proxy

/**
 * Use this extension method if you need to play a media stream that is restricted in your country.
 * @param streamUrl - media stream URL.
 * @param proxy - net.Proxy implementation from [org.iif.smart_proxy.data.ProxyManager].
 * @param playWhenReady - whether to start playback immediately.
 */
fun ExoPlayer.playStream(data: StreamData, proxy: Proxy, playWhenReady: Boolean = true): ExoPlayer {
    when (data.type) {
        StreamType.HLS -> this.setupHls(data.url, proxy)
        else -> this.setupStream(data.url)
    }
    prepare()
    this.playWhenReady = playWhenReady
    return this
}


@OptIn(UnstableApi::class)
private fun ExoPlayer.setupHls(url: String?, proxy: Proxy) {
    val client = OkHttpClient.Builder().proxy(proxy).build()
    val factory = OkHttpDataSource.Factory(client)
    val hlsMediaSource = HlsMediaSource.Factory(factory)
        .createMediaSource(MediaItem.fromUri(url ?: ""))
    setMediaSource(hlsMediaSource)
}

private fun ExoPlayer.setupStream(url: String?) {
    setMediaItem(MediaItem.fromUri(url ?: ""))
}
