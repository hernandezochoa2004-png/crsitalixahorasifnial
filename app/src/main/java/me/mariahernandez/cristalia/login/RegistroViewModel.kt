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

data class RegistroUiState(
    val usuario: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val cargando: Boolean = false,
    val error: String? = null
)

sealed interface RegistroEventoUi {
    data object RegistroExitoso : RegistroEventoUi
    data object NavegarALogin : RegistroEventoUi
}

class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    private val _eventos = Channel<RegistroEventoUi>(Channel.BUFFERED)
    val eventos = _eventos.receiveAsFlow()

    fun onUsuarioChange(nuevoUsuario: String) {
        _uiState.update { it.copy(usuario = nuevoUsuario, error = null) }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo, error = null) }
    }

    fun onContrasenaChange(nuevaContrasena: String) {
        _uiState.update { it.copy(contrasena = nuevaContrasena, error = null) }
    }

    fun onConfirmarContrasenaChange(nuevaConfirmacion: String) {
        _uiState.update { it.copy(confirmarContrasena = nuevaConfirmacion, error = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun onCrearCuentaClick() {
        val currentState = _uiState.value
        if (currentState.usuario.isBlank() || currentState.correo.isBlank() || currentState.contrasena.isBlank() || currentState.confirmarContrasena.isBlank()) {
            _uiState.update { it.copy(error = "Por favor completa todos los campos") }
            return
        }

        if (currentState.contrasena != currentState.confirmarContrasena) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            _uiState.update { it.copy(cargando = false) }
            _eventos.send(RegistroEventoUi.RegistroExitoso)
        }
    }

    fun onIrALoginClick() {
        viewModelScope.launch {
            _eventos.send(RegistroEventoUi.NavegarALogin)
        }
    }
}
