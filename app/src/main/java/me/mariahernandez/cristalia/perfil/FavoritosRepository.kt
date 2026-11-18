package me.mariahernandez.cristalia.perfil

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoritosRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val userId: String?
        get() = auth.currentUser?.uid

    suspend fun guardarFavorito(item: OperacionHistorial) {
        val uid = userId ?: return
        try {
            db.collection("users")
                .document(uid)
                .collection("favoritos")
                .document(item.id)
                .set(item)
                .await()
        } catch (_: Exception) {}
    }

    suspend fun eliminarFavorito(itemId: String) {
        val uid = userId ?: return
        try {
            db.collection("users")
                .document(uid)
                .collection("favoritos")
                .document(itemId)
                .delete()
                .await()
        } catch (_: Exception) {}
    }

    suspend fun obtenerFavoritos(): List<OperacionHistorial> {
        val uid = userId ?: return emptyList()
        return try {
            val snapshot = db.collection("users")
                .document(uid)
                .collection("favoritos")
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(OperacionHistorial::class.java)?.copy(esFavorito = true)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
