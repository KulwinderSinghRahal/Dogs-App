package apps.kulwinder.dogapp.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apps.kulwinder.dogapp.model.Breed
import apps.kulwinder.dogapp.repository.ApiResponse
import apps.kulwinder.dogapp.repository.DogsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: DogsRepository,
) : ViewModel() {

    private val _breeds = MutableStateFlow<State>(State(isLoading = true))
    val breeds = _breeds.asStateFlow()

    init {
        loadBreeds()
    }

    private fun loadBreeds() {
        viewModelScope.launch {
            when (val response = repository.getBreeds()) {
                is ApiResponse.Success -> _breeds.update { state ->
                    state.copy(isLoading = false, breeds = response.data)
                }
                is ApiResponse.Error -> _breeds.update {
                    it.copy(isLoading = false, error = "Failed to load!")
                }
            }
        }
    }


    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val breeds: List<Breed> = emptyList()
    )

}