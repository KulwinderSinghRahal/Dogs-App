package apps.kulwinder.dogapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apps.kulwinder.dogapp.model.Breed
import apps.kulwinder.dogapp.ui.base.AppToolbar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onNavigateToBreedDetail: (Breed) -> Unit,
    onNavigateToFavorites: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val breedsState by viewModel.breeds.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Breeds",
                actions = {
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
            )
        },
        backgroundColor = Color.LightGray.copy(alpha = 0.3f),
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (breedsState.isLoading)
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                else
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(breedsState.breeds, key = { it.name }) { breed ->
                            Card(
                                elevation = 4.dp,
                            ) {
                                ListItem(
                                    text = {
                                        Text(text = breed.name, fontWeight = FontWeight.Bold)
                                    },
                                    secondaryText = {
                                        Column(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            for (subBreed in breed.subBreeds)
                                                Text(text = subBreed)
                                        }
                                    },
                                    modifier = Modifier.clickable { onNavigateToBreedDetail(breed) }
                                )
                            }
                        }
                    }
            }
        },
    )
}