package apps.kulwinder.dogapp.ui.detail


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apps.kulwinder.dogapp.model.Breed
import apps.kulwinder.dogapp.ui.base.AppToolbar
import apps.kulwinder.dogapp.ui.base.LoadingShimmerEffect
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun DetailScreen(
    breed: Breed,
    onBack: (() -> Unit)? = null,
    vm: DetailScreenViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState()

    Scaffold(topBar = {
        AppToolbar(title = state.breed.name, onBackClicked = onBack)
    }, floatingActionButton = {
        FloatingActionButton(onClick = { vm.onFavoriteClicked() }) {
            Icon(
                imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null
            )
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight(.35f)
                    .fillMaxWidth()
            ) {
                if (!state.isLoading)
                    HorizontalPager(
                        pageCount = state.breedImages.size,
                        contentPadding = PaddingValues(horizontal = 42.dp),
                        state = pagerState,
                    ) { page ->
                        SubcomposeAsyncImage(
                            model = state.breedImages[page],

                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    // Calculate the absolute offset for the current page from the
                                    // scroll position. We use the absolute value which allows us to mirror
                                    // any effects for both directions
                                    val pageOffset =
                                        pagerState.calculateCurrentOffsetForPage(page).absoluteValue

                                    // We animate the scaleX + scaleY, between 85% and 100%
                                    lerp(
                                        start = 0.85f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    ).also { scale ->
                                        scaleX = scale
                                        scaleY = scale
                                    }

                                    // We animate the alpha, between 50% and 100%
                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )
                                },
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                        ) {
                            val state = painter.state
                            AnimatedContent(targetState = state) {
                                when (state) {
                                    is AsyncImagePainter.State.Loading -> LoadingShimmerEffect(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                    is AsyncImagePainter.State.Error -> {
                                        Text(
                                            text = "Failed to load !",
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                        )
                                    }
                                    else -> SubcomposeAsyncImageContent()
                                }
                            }
                        }
                    }
                else
                    LoadingShimmerEffect(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 42.dp)
                    )
            }
            if (state.breed.subBreeds.isNotEmpty()) {
                Column(
                    Modifier.padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(text = "Sub-Breeds", fontWeight = FontWeight.W400, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.breed.subBreeds) { item ->
                            Chip(onClick = { }) {
                                Text(text = item, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage + currentPageOffsetFraction) - page
}