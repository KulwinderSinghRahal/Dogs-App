package apps.kulwinder.dogapp.ui.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apps.kulwinder.dogapp.model.Breed
import apps.kulwinder.dogapp.repository.DogsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dogsLocalRepository: DogsLocalRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(State(isLoading = true))
    val state = _state.asStateFlow()


    init {
        loadBreedImages()
    }

    private fun loadBreedImages() {
        viewModelScope.launch {
            val response = dogsLocalRepository.getAllFavoriteBreeds()
            _state.update {
                it.copy(
                    isLoading = false,
                    breeds = response,
                )
            }

        }
    }


    data class State(
        val isLoading: Boolean = false,
        val breeds: List<Breed> = emptyList(),
        val error: String? = null,
    )

}