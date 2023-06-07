package ru.mrmarvel.camoletapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrmarvel.camoletapp.R
import ru.mrmarvel.camoletapp.customcamoletappbar.CustomCamoletAppBar
import ru.mrmarvel.camoletapp.profilelabel.ProfileLabel


@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navigateToEditProfileScreen: () ->Unit ={}) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
        CustomCamoletAppBar(modifier.fillMaxWidth(), labelText = "ПРОФИЛЬ",
            leftImage = painterResource(id = R.drawable.burger_image),
            showLeftImage = true,
            showRightImage = true,
        )
    }) {padding ->
        Surface(
            Modifier
                .padding(padding)
                .padding(15.dp)
                .fillMaxSize()
        ) {
            Column(
                Modifier.fillMaxWidth()
            ) {
                /*IconButton(
                    modifier = Modifier
                        .size(24.dp)
                        .align(End),
                    onClick = {

                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.edit_image),
                        contentDescription = "Edit profile")
                }*/
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Text("ID", Modifier.padding(bottom = 1.dp))
                    ProfileLabel(
                        Modifier.padding(bottom = 16.dp),
                        text = "120894")
                    val elementModifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    val elementLabelModifier = Modifier.padding(bottom = 1.dp).fillMaxWidth()
                    Column {
                        Text("Фамилия", elementLabelModifier)
                        ProfileLabel(elementModifier, text = "Унгер")
                        Text("Имя", elementLabelModifier)
                        ProfileLabel(elementModifier, text = "Антон")
                        Text("Отчество", elementLabelModifier)
                        ProfileLabel(elementModifier, text = "Юрьевич")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Avatar() {

}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

@Preview
@Composable
fun CustomCamoletAppBarPreview() {
    CustomCamoletAppBar()
}