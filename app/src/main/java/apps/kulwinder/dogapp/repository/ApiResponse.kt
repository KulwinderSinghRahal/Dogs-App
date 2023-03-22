package apps.kulwinder.dogapp.repository


sealed class ApiResponse<T> {

    data class Success<T>(val data: T) : ApiResponse<T>()

    data class Error<T>(val exception: Exception) : ApiResponse<T>()

}