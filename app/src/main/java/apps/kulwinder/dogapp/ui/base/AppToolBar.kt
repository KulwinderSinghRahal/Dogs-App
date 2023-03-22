package apps.kulwinder.dogapp.ui.base

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun AppToolbar(
    title: String,
    onBackClicked: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        navigationIcon = if (onBackClicked != null) {
            {
                IconButton(onClick = { onBackClicked() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        } else null,
        title = {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        },
        actions = actions
    )
}