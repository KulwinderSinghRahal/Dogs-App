package apps.kulwinder.dogapp.repository

import apps.kulwinder.dogapp.data.local.breeds.BreedsDao
import apps.kulwinder.dogapp.model.Breed
import javax.inject.Inject

class DogsLocalRepository @Inject constructor(
    private val breedsDao: BreedsDao
) {
    suspend fun saveBreedAsFavorite(breed: Breed) {
        breedsDao.insert(breed)
    }

    suspend fun removeBreedFromFavorite(breed: Breed) {
        breedsDao.delete(breed)
    }

    suspend fun isBreedInFavorite(breed: Breed): Boolean {
        return breedsDao.loadByName(breed.name) != null
    }

    suspend fun getAllFavoriteBreeds(): List<Breed> {
        return breedsDao.getAll()
    }
}