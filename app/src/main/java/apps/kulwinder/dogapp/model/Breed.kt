package apps.kulwinder.dogapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Breed(
    @PrimaryKey
    val name: String,
    val subBreeds: List<String>
)

