package me.mariahernandez.cristalia.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val usuario: String = "",
    val contrasena: String = "",
    val passwordVisible: Boolean = false,
    val cargando: Boolean = false,
    val error: String? = null
)

sealed interface LoginEventoUi {
    data class LoginExitoso(val usuario: String) : LoginEventoUi
    data object NavegarACrearCuenta : LoginEventoUi
    data object OlvideContrasena : LoginEventoUi
}

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _eventos = Channel<LoginEventoUi>(Channel.BUFFERED)
    val eventos = _eventos.receiveAsFlow()

    fun onUsuarioChange(nuevoUsuario: String) {
        _uiState.update { it.copy(usuario = nuevoUsuario, error = null) }
    }

    fun onContrasenaChange(nuevaContrasena: String) {
        _uiState.update { it.copy(contrasena = nuevaContrasena, error = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onIniciarSesionClick() {
        val currentState = _uiState.value
        if (currentState.usuario.isBlank() || currentState.contrasena.isBlank()) {
            _uiState.update { it.copy(error = "Por favor ingresa usuario y contraseña") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            _uiState.update { it.copy(cargando = false) }
            _eventos.send(LoginEventoUi.LoginExitoso(currentState.usuario))
        }
    }

    fun onCrearCuentaClick() {
        viewModelScope.launch {
            _eventos.send(LoginEventoUi.NavegarACrearCuenta)
        }
    }

    fun onOlvideContrasenaClick() {
        viewModelScope.launch {
            _eventos.send(LoginEventoUi.OlvideContrasena)
        }
    }
}
