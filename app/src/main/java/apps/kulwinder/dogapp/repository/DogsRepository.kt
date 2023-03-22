package apps.kulwinder.dogapp.repository

import apps.kulwinder.dogapp.data.network.DogsServiceImpl
import apps.kulwinder.dogapp.model.Breed
import javax.inject.Inject

class DogsRepository @Inject constructor(
    private val dogsService: DogsServiceImpl
) {

    suspend fun getBreeds(): ApiResponse<List<Breed>> {
        return apiCall {
            dogsService.getDogBreeds().message.map { Breed(it.key, it.value) }
        }
    }

    suspend fun getImagesByBreed(breed: Breed): ApiResponse<List<String>> {
        return apiCall {
            dogsService.getImagesByBreed(breed.name).images
        }
    }

    private suspend inline fun <T> apiCall(
        call: suspend () -> T
    ): ApiResponse<T> {
        return try {
            ApiResponse.Success(call())
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }
}