package movies

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavRoute(
    val route: String,
    val icon: ImageVector,
    val label: String,
)

val bottomNavItems =
    listOf(
        BottomNavRoute(
            route = Router.POPULAR,
            icon = Icons.Default.Home,
            label = "Popular",
        ),
        BottomNavRoute(
            route = Router.TOP_RATED,
            icon = Icons.Default.Search,
            label = "Mejor valorado",
        ),
        BottomNavRoute(
            route = Router.UPCOMING,
            icon = Icons.Default.AccountCircle,
            label = "Próximamente",
        ),
    )
