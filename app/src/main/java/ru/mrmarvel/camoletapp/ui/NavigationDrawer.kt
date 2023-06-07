package ru.mrmarvel.camoletapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrmarvel.camoletapp.R
import ru.mrmarvel.camoletapp.drawernavigationitem.DrawerNavigationItem
import ru.mrmarvel.camoletapp.ui.theme.CamoletTheme

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit = {},
    navigateToMonitoringScreen: () -> Unit = {},
    navigateToHelpScreen: () -> Unit = {}
) {
    CamoletTheme() {
        val backgroundColor: Color = MaterialTheme.colors.primary
        Surface(
            modifier
                .background(Color.Transparent)
        ) {
            Column(
                Modifier.background(backgroundColor)
                    .padding(10.dp)
                    .width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = closeDrawer,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Start)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.burger_image),
                        contentDescription = "Close drawer",
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
                val itemModifier = Modifier.padding(16.dp)
                DrawerNavigationItem(modifier = itemModifier, text = "МОНИТОРИНГ",
                    onItemClick = navigateToMonitoringScreen)
                DrawerNavigationItem(modifier =  itemModifier, text = "СПРАВКА",
                    onItemClick = navigateToHelpScreen)
            }
        }
    }
}

@Preview
@Composable
fun NavigationDrawerPreview() {
    NavigationDrawer()
}

@Preview
@Composable
fun DrawerNavigationItemPreview() {
    DrawerNavigationItem(text = "МОНИТОРИНГ")
}