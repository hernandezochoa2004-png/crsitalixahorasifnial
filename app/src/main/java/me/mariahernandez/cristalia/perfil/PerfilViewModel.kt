package me.mariahernandez.cristalia.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PerfilEventoUi {
    data object Volver : PerfilEventoUi
    data object CerrarSesion : PerfilEventoUi
}

class PerfilViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        PerfilUiState(
            historial = datosEjemploHistorial()
        )
    )
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    private val _eventos = Channel<PerfilEventoUi>(Channel.BUFFERED)
    val eventos = _eventos.receiveAsFlow()

    fun inicializarUsuario(usuarioRaw: String?) {
        if (!usuarioRaw.isNullOrBlank()) {
            val nombreLimpio = usuarioRaw.trim()
            val nombreFormateado = if (nombreLimpio.contains("@")) {
                nombreLimpio.substringBefore("@")
                    .replace(".", " ")
                    .replace("_", " ")
                    .capitalizeWords()
            } else {
                nombreLimpio.replace("_", " ").capitalizeWords()
            }
            _uiState.update { it.copy(nombreUsuario = nombreFormateado) }
        }
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }

    fun seleccionarTab(tab: TabPerfil) {
        _uiState.update { it.copy(tabSeleccionado = tab) }
    }

    fun toggleFavorito(id: String) {
        _uiState.update { state ->
            val nuevoHistorial = state.historial.map { item ->
                if (item.id == id) {
                    item.copy(esFavorito = !item.esFavorito)
                } else {
                    item
                }
            }
            state.copy(historial = nuevoHistorial)
        }
    }

    fun onVolverClick() {
        viewModelScope.launch {
            _eventos.send(PerfilEventoUi.Volver)
        }
    }

    fun onCerrarSesionClick() {
        viewModelScope.launch {
            _eventos.send(PerfilEventoUi.CerrarSesion)
        }
    }

    companion object {
        fun datosEjemploHistorial(): List<OperacionHistorial> = listOf(
            OperacionHistorial(
                id = "1",
                titulo = "NaCl (Halita)",
                descripcion = "Red FCC - Estructura tipo AB",
                tipoEstructura = "FCC",
                badges = listOf(
                    OperacionBadge("NC:", "6:6"),
                    OperacionBadge("ρ:", "2.14 g/cm³")
                ),
                esFavorito = true
            ),
            OperacionHistorial(
                id = "2",
                titulo = "Cúbica Simple (SC)",
                descripcion = "Polonio-α - Celda primitiva",
                tipoEstructura = "SC",
                badges = listOf(
                    OperacionBadge("APF:", "0.52"),
                    OperacionBadge("NC:", "6")
                ),
                esFavorito = true
            ),
            OperacionHistorial(
                id = "3",
                titulo = "Rutilo (TiO₂)",
                descripcion = "Estructura tipo AB₂ - Tetragonal",
                tipoEstructura = "TETR",
                badges = listOf(
                    OperacionBadge("NC:", "6:3"),
                    OperacionBadge("c/a:", "0.644")
                ),
                esFavorito = true
            ),
            OperacionHistorial(
                id = "4",
                titulo = "CsCl (Cloruro de Cesio)",
                descripcion = "Red Cúbica Centrada en el Cuerpo - AB",
                tipoEstructura = "BCC",
                badges = listOf(
                    OperacionBadge("NC:", "8:8"),
                    OperacionBadge("ρ:", "3.99 g/cm³")
                ),
                esFavorito = false
            ),
            OperacionHistorial(
                id = "5",
                titulo = "Blenda de Zinc (ZnS)",
                descripcion = "Red FCC con huecos tetraédricos ocupados",
                tipoEstructura = "FCC",
                badges = listOf(
                    OperacionBadge("NC:", "4:4"),
                    OperacionBadge("APF:", "0.68")
                ),
                esFavorito = false
            ),
            OperacionHistorial(
                id = "6",
                titulo = "Factor Empaquetamiento (FCC)",
                descripcion = "Cobre / Aluminio - Eficiencia de llenado",
                tipoEstructura = "FE",
                badges = listOf(
                    OperacionBadge("APF:", "0.74"),
                    OperacionBadge("NC:", "12")
                ),
                esFavorito = true
            ),
            OperacionHistorial(
                id = "7",
                titulo = "Huecos Octaédricos (FCC)",
                descripcion = "4 Huecos por celda - Posición aristas y centro",
                tipoEstructura = "HUECO",
                badges = listOf(
                    OperacionBadge("NC:", "6"),
                    OperacionBadge("r/R:", "0.414")
                ),
                esFavorito = true
            ),
            OperacionHistorial(
                id = "8",
                titulo = "Grafito (Hexagonal)",
                descripcion = "Estructura HCP laminar con enlaces sp²",
                tipoEstructura = "HCP",
                badges = listOf(
                    OperacionBadge("c/a:", "2.72"),
                    OperacionBadge("ρ:", "2.26 g/cm³")
                ),
                esFavorito = true
            )
        )
    }
}
