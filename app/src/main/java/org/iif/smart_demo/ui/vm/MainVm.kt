package org.iif.smart_demo.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.iif.smart_demo.domain.ConnectState
import org.iif.smart_demo.domain.Stream
import org.iif.smart_demo.repo.MainRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.Proxy
import javax.inject.Inject

@HiltViewModel
class MainVm @Inject constructor(private val _repo: MainRepo) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    fun fetchData() = viewModelScope.launch {
        try {
            _repo.startProxy()
            val data = _repo.getSmartProxyMedia()
            _state.value = AppState(
                isLoading = false,
                connectionStatus = ConnectState.CONNECTED,
                stream = data,
                proxy = _repo.getProxy()
            )
        } catch (e: Throwable) {
            handleException(e)
        }
    }

    private fun handleException(e: Throwable) {
        _state.value = AppState(
            isLoading = false,
            connectionStatus = ConnectState.ERROR,
            error = e.message
        )
    }

    fun stopProxy() = viewModelScope.launch {
        try {
            _repo.stopProxy()
        } catch (e: Exception) {
            handleException(e)
        }
    }
}

data class AppState(
    val error: String? = null,
    val isLoading: Boolean = true,
    val connectionStatus: ConnectState = ConnectState.DISCONNECTED,
    val stream: Stream? = null,
    val proxy: Proxy? = null
)