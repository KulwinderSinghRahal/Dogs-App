package apps.kulwinder.dogapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import apps.kulwinder.dogapp.data.local.breeds.BreedsDao
import apps.kulwinder.dogapp.model.Breed

@Database(entities = [Breed::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breedsDao(): BreedsDao
}