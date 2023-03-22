package apps.kulwinder.dogapp.di

import android.content.Context
import androidx.room.Room
import apps.kulwinder.dogapp.data.local.AppDatabase
import apps.kulwinder.dogapp.data.local.breeds.BreedsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context = applicationContext,
            klass = AppDatabase::class.java,
            name = "dogs-database"
        ).build()

    }

    @Provides
    @Singleton
    fun provide(appDatabase: AppDatabase): BreedsDao {
        return appDatabase.breedsDao()
    }
}