package apps.kulwinder.dogapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apps.kulwinder.dogapp.Route
import apps.kulwinder.dogapp.model.Breed
import apps.kulwinder.dogapp.repository.ApiResponse
import apps.kulwinder.dogapp.repository.DogsLocalRepository
import apps.kulwinder.dogapp.repository.DogsRepository
import apps.kulwinder.dogapp.utils.fromJson
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: DogsRepository,
    private val dogsLocalRepository: DogsLocalRepository,
) : ViewModel() {

    private val breed by lazy {
        savedStateHandle.get<String>(Route.Detail.ARG_BREED)
            ?.let { Gson().fromJson<Breed>(it) }
    }

    private val _state = MutableStateFlow(State(isLoading = true, breed = breed!!))
    val state = _state.asStateFlow()

    init {
        loadBreedImages()
    }

    private fun loadBreedImages() {
        viewModelScope.launch {
            val response = repository.getImagesByBreed(breed!!)
            when (response) {
                is ApiResponse.Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        breedImages = response.data,
                        isFavorite = dogsLocalRepository.isBreedInFavorite(breed!!)
                    )
                }
                is ApiResponse.Error -> _state.update {
                    it.copy(isLoading = false, error = "Failed to load images!")
                }
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (state.value.isFavorite)
                dogsLocalRepository.removeBreedFromFavorite(breed!!)
            else
                dogsLocalRepository.saveBreedAsFavorite(breed!!)
            _state.update {
                it.copy(isFavorite = dogsLocalRepository.isBreedInFavorite(breed!!))
            }
        }
    }


    data class State(
        val isLoading: Boolean = false,
        val breedImages: List<String> = emptyList(),
        val breed: Breed,
        val isFavorite: Boolean = false,
        val error: String? = null,
    )
}