package apps.kulwinder.dogapp

import apps.kulwinder.dogapp.model.Breed
import apps.kulwinder.dogapp.repository.ApiResponse
import apps.kulwinder.dogapp.repository.DogsRepository
import apps.kulwinder.dogapp.ui.home.HomeScreenViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.Test

class HomeScreenViewModelTest {

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var repository: DogsRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadBreedsSuccessTest() = runTest {
        repository = mockk()
        coEvery { repository.getBreeds() } returns ApiResponse.Success(
            listOf(
                Breed("Husky", emptyList()),
                Breed("Pitbull", emptyList()),
            )
        )
        viewModel = HomeScreenViewModel(
            savedStateHandle = mockk(relaxed = true),
            repository = repository
        )
        advanceUntilIdle()
        coVerify { repository.getBreeds() }

        assert(viewModel.breeds.value.breeds.size == 2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadBreedsSuccess_emptyBreeds_Test() = runTest {
        repository = mockk()
        coEvery { repository.getBreeds() } returns ApiResponse.Success(emptyList())
        viewModel = HomeScreenViewModel(
            savedStateHandle = mockk(relaxed = true),
            repository = repository
        )
        advanceUntilIdle()
        coVerify { repository.getBreeds() }

        assert(viewModel.breeds.value.breeds.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadBreedsFailedTest() = runTest {
        repository = mockk()
        coEvery { repository.getBreeds() } returns ApiResponse.Error(
            java.lang.IllegalStateException()
        )

        viewModel = HomeScreenViewModel(
            savedStateHandle = mockk(relaxed = true),
            repository = repository
        )
        advanceUntilIdle()
        coVerify { repository.getBreeds() }

        assert(viewModel.breeds.value.breeds.isEmpty())
        assert(viewModel.breeds.value.error != null)
    }
}