package apps.kulwinder.dogapp.data.network.response

import com.google.gson.annotations.SerializedName

data class BreedImagesResponse(
    @SerializedName("message")
    val images: List<String>
)
