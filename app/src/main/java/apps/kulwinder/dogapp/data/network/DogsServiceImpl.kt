package apps.kulwinder.dogapp.data.network

import apps.kulwinder.dogapp.data.network.response.BreedImagesResponse
import apps.kulwinder.dogapp.data.network.response.DogBreedsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsServiceImpl {

    @GET("/api/breeds/list/all")
    suspend fun getDogBreeds(): DogBreedsResponse

    @GET("/api/breed/{breed}/images")
    suspend fun getImagesByBreed(
        @Path("breed") breedName: String,
    ): BreedImagesResponse

}