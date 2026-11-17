package me.mariahernandez.cristalia.perfil

data class OperacionBadge(
    val etiqueta: String,
    val valor: String
)

data class OperacionHistorial(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipoEstructura: String = "FCC",
    val badges: List<OperacionBadge> = emptyList(),
    val esFavorito: Boolean = false
)

enum class TabPerfil {
    FAVORITOS,
    HISTORIAL
}

data class PerfilUiState(
    val nombreUsuario: String = "Laura Martínez",
    val totalCalculos: Int = 14,
    val tabSeleccionado: TabPerfil = TabPerfil.FAVORITOS,
    val historial: List<OperacionHistorial> = emptyList()
) {
    val totalFavoritos: Int get() = historial.count { it.esFavorito }
    val favoritosFiltrados: List<OperacionHistorial> get() = historial.filter { it.esFavorito }
}
