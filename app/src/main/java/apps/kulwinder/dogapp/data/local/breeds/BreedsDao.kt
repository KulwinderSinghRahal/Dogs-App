package apps.kulwinder.dogapp.data.local.breeds

import androidx.room.*
import apps.kulwinder.dogapp.model.Breed

@Dao
interface BreedsDao {
    @Query("SELECT * FROM breed")
    suspend fun getAll(): List<Breed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(breed: Breed)

    @Query("SELECT * from breed WHERE name LIKE :name")
    suspend fun loadByName(name: String): Breed?

    @Delete
    suspend fun delete(breed: Breed)
}