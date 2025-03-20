package org.iif.smart_demo.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import org.iif.smart_demo.R
import org.iif.smart_demo.domain.ConnectState
import org.iif.smart_demo.mappers.toStreamData
import org.iif.smart_demo.ui.components.ProgramList
import org.iif.smart_demo.ui.components.VideoPlayer
import org.iif.smart_demo.ui.theme.OutlineMediaTheme
import org.iif.smart_demo.ui.vm.AppState
import org.iif.smart_demo.ui.vm.MainVm

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val vm by viewModels<MainVm>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        vm.fetchData()
        setContent {
            OutlineMediaTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Smart Proxy Demo") }) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val state by vm.state.collectAsState()
                    Content(Modifier.padding(innerPadding), state)
                }
            }
        }
    }

    override fun onDestroy() {
        vm.stopProxy()
        super.onDestroy()
    }

    @Composable
    private fun Content(modifier: Modifier, state: AppState) {

        when (state.connectionStatus) {
            ConnectState.CONNECTED -> MediaContent(modifier, state)
            ConnectState.ERROR -> ContentLoader(
                modifier,
                ImageVector.vectorResource(R.drawable.baseline_wifi_tethering_off_24),
                state.error ?: "Error"
            )

            else -> ContentLoader(
                modifier,
                ImageVector.vectorResource(R.drawable.baseline_wifi_tethering_24),
                "Connecting..."
            )
        }
    }

    @Composable
    private fun ContentLoader(modifier: Modifier, vector: ImageVector, message: String) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = vector,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp)
            )
            Text(
                text = message,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    @Composable
    private fun MediaContent(modifier: Modifier, state: AppState) {
        val scrollState = rememberScrollState()
        Column(modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {
            VideoPlayer(
                streamData = state.stream?.toStreamData(),
                tvShowName = state.stream?.currentShowName,
                proxy = state.proxy,
                onPlayerError = ::connectWithTimeout
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProgramList(state.stream?.nextTimeSlots?.take(5))
        }
    }

    private fun connectWithTimeout(
        error: PlaybackException,
        player: ExoPlayer,
        timeoutMs: Long = 3000
    ) {
        if (error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED) {
            Handler(Looper.getMainLooper()).postDelayed({ player.prepare() }, timeoutMs)
        }
    }
}