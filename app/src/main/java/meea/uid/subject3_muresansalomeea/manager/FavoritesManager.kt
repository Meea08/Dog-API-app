package meea.uid.subject3_muresansalomeea.manager

import android.content.Context

class FavoritesManager(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("FavoritesPreferences", Context.MODE_PRIVATE)

    fun addFavorite(breed: String, imageUrl: String) {
        val favorites = getFavorites(breed).toMutableSet()
        favorites.add(imageUrl)
        saveFavorites(breed, favorites)
    }

    fun removeFavorite(breed: String, imageUrl: String) {
        val favorites = getFavorites(breed).toMutableSet()
        favorites.remove(imageUrl)
        saveFavorites(breed, favorites)
    }

    fun getFavorites(breed: String): Set<String> {
        return sharedPreferences.getStringSet(breed, setOf()) ?: setOf()
    }

    private fun saveFavorites(breed: String, favorites: Set<String>) {
        sharedPreferences.edit().putStringSet(breed, favorites).apply()
    }

    fun isFavorite(breed: String, imageUrl: String): Boolean {
        return getFavorites(breed).contains(imageUrl)
    }
}
