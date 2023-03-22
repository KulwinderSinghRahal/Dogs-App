package apps.kulwinder.dogapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import apps.kulwinder.dogapp.model.Breed
import apps.kulwinder.dogapp.ui.detail.DetailScreen
import apps.kulwinder.dogapp.ui.favorites.FavoritesScreen
import apps.kulwinder.dogapp.ui.home.HomeScreen
import apps.kulwinder.dogapp.ui.theme.DogAppTheme
import apps.kulwinder.dogapp.utils.fromJson
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Route.Home.nameWithArg) {
                    composable(Route.Home.nameWithArg) {
                        HomeScreen(
                            onNavigateToBreedDetail = {
                                val routeWithArg = Route.Detail.makeRoute(it)
                                navController.navigate(routeWithArg)
                            },
                            onNavigateToFavorites = {
                                navController.navigate(Route.Favorites.nameWithArg)
                            }
                        )
                    }
                    composable(Route.Favorites.nameWithArg) {
                        FavoritesScreen(
                            onNavigateToBreedDetail = {
                                val routeWithArg = Route.Detail.makeRoute(it)
                                navController.navigate(routeWithArg)
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(Route.Detail().nameWithArg) {
                        val breed = Route.Detail.parseBreed(it.arguments)
                        if (breed != null)
                            DetailScreen(
                                breed,
                                onBack = { navController.popBackStack() }
                            )
                    }
                }
            }
        }
    }
}

sealed class Route(val nameWithArg: String) {
    object Home : Route("home")
    object Favorites : Route("favorites")
    class Detail : Route("detail/{$ARG_BREED}") {
        companion object {
            const val ARG_BREED = "breed"
            fun makeRoute(breed: Breed) = Detail().nameWithArg
                .replace("{$ARG_BREED}", Gson().toJson(breed))

            fun parseBreed(arguments: Bundle?): Breed? {
                val string = arguments?.getString(ARG_BREED, "")
                return string
                    ?.let { json -> Gson().fromJson<Breed>(json) }
            }
        }
    }
}