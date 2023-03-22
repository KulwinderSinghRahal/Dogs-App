package apps.kulwinder.dogapp.repository

import apps.kulwinder.dogapp.data.network.DogsServiceImpl
import apps.kulwinder.dogapp.data.network.response.BreedImagesResponse
import apps.kulwinder.dogapp.data.network.response.DogBreedsResponse
import apps.kulwinder.dogapp.model.Breed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DogsRepositoryTest {

    private lateinit var repository: DogsRepository
    private lateinit var fakeDogsService: DogsServiceImpl

    @Before
    fun setup() {
        fakeDogsService = mockk()
        repository = DogsRepository(
            dogsService = fakeDogsService,
        )
    }

    @Test
    fun getBreedsSuccessTest() {
        coEvery { fakeDogsService.getDogBreeds() } returns DogBreedsResponse(
            mapOf(
                "dog1" to emptyList(),
                "dog2" to emptyList(),
            )
        )
        val breeds = runBlocking { repository.getBreeds() }
        assert(breeds is ApiResponse.Success && breeds.data.size == 2)
        coVerify { fakeDogsService.getDogBreeds() }
    }

    @Test
    fun loadBreedsFailedTest() {
        coEvery { fakeDogsService.getDogBreeds() } throws IllegalStateException()
        val breeds = runBlocking { repository.getBreeds() }
        assert(breeds is ApiResponse.Error && breeds.exception is IllegalStateException)
        coVerify { fakeDogsService.getDogBreeds() }
    }

    @Test
    fun getImages_byBreed_successTest() {
        coEvery { fakeDogsService.getImagesByBreed(any()) } returns BreedImagesResponse(
            listOf(
                "image1",
                "image2",
            )
        )
        val breeds = runBlocking { repository.getImagesByBreed(Breed("husky", emptyList())) }
        assert(breeds is ApiResponse.Success && breeds.data.size == 2)
        coVerify { fakeDogsService.getImagesByBreed("husky") }
    }

    @Test
    fun getImages_byBreed_failedTest() {
        coEvery {
            fakeDogsService.getImagesByBreed(any())
        } throws IllegalStateException()
        val breeds = runBlocking { repository.getImagesByBreed(Breed("husky", emptyList())) }
        assert(breeds is ApiResponse.Error && breeds.exception is IllegalStateException)
        coVerify { fakeDogsService.getImagesByBreed("husky") }
    }
}