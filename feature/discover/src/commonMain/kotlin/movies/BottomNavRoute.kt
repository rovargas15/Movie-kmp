package movies

import Router
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavRoute(
    val route: String,
    val icon: ImageVector,
    val label: String,
)

val bottomNavItems =
    listOf(
        BottomNavRoute(
            route = Router.DISCOVER,
            icon = Icons.Default.Home,
            label = "Popular",
        ),
        BottomNavRoute(
            route = Router.FAVORITE,
            icon = Icons.Default.Favorite,
            label = "favorite",
        )
    )
