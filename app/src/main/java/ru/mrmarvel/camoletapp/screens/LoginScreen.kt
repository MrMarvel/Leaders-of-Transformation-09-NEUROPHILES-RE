package ru.mrmarvel.camoletapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mrmarvel.camoletapp.R
import ru.mrmarvel.camoletapp.bigappname.BigAppName
import ru.mrmarvel.camoletapp.blue1linebutton.Blue1lineButton
import ru.mrmarvel.camoletapp.data.LoginScreenViewModel

@Composable
fun LoginScreen(
    loginScreenViewModel: LoginScreenViewModel,
    navigateToMonitoringScreen: () -> Unit = {},
) {
    val loginError = remember {loginScreenViewModel.loginError}
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Surface(
        Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(30.dp)
        ) {
            BigAppName(
                text = stringResource(id = R.string.company_name),
                modifier = Modifier.padding(bottom = 50.dp)
            )
            val inputModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
            TextField(
                modifier = inputModifier,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.user_icon),
                        contentDescription = "Login icon"
                    )
                },
                value = username.value,
                onValueChange = {username.value = it},
                placeholder = {Text("BungerUsername")},
                singleLine = true,
                label = {Text("Логин")}
            )
            TextField(
                modifier = inputModifier,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.lock_icon),
                        contentDescription = "Password icon"
                    )
                },
                placeholder = {Text("coolpassword1234")},
                value = password.value,
                onValueChange = {password.value = it},
                singleLine = true,
                label = {Text("Пароль")}
            )
            Blue1lineButton(
                buttonText = "ВОЙТИ",
                onItemClicked = {

                },
                modifier = Modifier.padding(top = 50.dp)
            )
        }
    }
}