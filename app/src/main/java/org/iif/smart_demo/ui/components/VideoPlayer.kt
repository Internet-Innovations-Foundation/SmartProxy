package org.iif.smart_demo.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.iif.smart_proxy.domain.StreamData
import org.iif.smart_proxy.utils.playStream
import java.net.Proxy

@Composable
fun VideoPlayer(
    streamData: StreamData?,
    tvShowName: String?,
    proxy: Proxy?,
    resumeInBackground: Boolean = true,
    onPlayerError: (error: PlaybackException, player: ExoPlayer) -> Unit = { _, _ -> }
) {
    if (streamData?.isValid() == false || tvShowName == null || proxy == null) {
        VideoPlayerError()
    } else {
        VideoPlayerContent(
            streamData = streamData!!,
            showName = tvShowName,
            proxy = proxy,
            resumeInBackground = resumeInBackground,
            onPlayerError = onPlayerError
        )
    }

}

@Composable
fun VideoPlayerError() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9),
        contentAlignment = Alignment.Center
    ) { Text("Error loading video...") }
}

@Composable
fun VideoPlayerContent(
    streamData: StreamData,
    showName: String,
    proxy: Proxy,
    resumeInBackground: Boolean = false,
    onPlayerError: (error: PlaybackException, player: ExoPlayer) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var playerState by remember { mutableIntStateOf(Player.STATE_IDLE) }
    val exoPlayer = remember { ExoPlayer.Builder(context).build().playStream(streamData, proxy) }

    DisposableEffect(exoPlayer) {

        val observer = LifecycleEventObserver { _, event ->
            when {
                resumeInBackground -> {}
                event == Lifecycle.Event.ON_PAUSE -> exoPlayer.playWhenReady = false
                event == Lifecycle.Event.ON_RESUME -> exoPlayer.playWhenReady = true
                event == Lifecycle.Event.ON_STOP -> exoPlayer.pause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        val stateListener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                playerState = state
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                onPlayerError(error, exoPlayer)
            }
        }
        exoPlayer.addListener(stateListener)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.removeListener(stateListener)
            exoPlayer.release()
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { context -> PlayerView(context).apply { player = exoPlayer } },
                modifier = Modifier.fillMaxSize()
            )
            if (playerState in listOf(Player.STATE_IDLE, Player.STATE_BUFFERING)) {
                CircularProgressIndicator()
            }

        }
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            text = showName
        )
    }
}
