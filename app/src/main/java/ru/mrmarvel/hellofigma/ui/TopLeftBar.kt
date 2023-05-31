package ru.mrmarvel.hellofigma.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrmarvel.hellofigma.changeflatbutton.ChangeFlatButton
import ru.mrmarvel.hellofigma.data.CameraScreenViewModel
import ru.mrmarvel.hellofigma.flatlabel.FlatLabel
import ru.mrmarvel.hellofigma.flatlock.FlatLock
import ru.mrmarvel.hellofigma.flatlock.IsLocked
import ru.mrmarvel.hellofigma.flatprogress.FlatProgress
import javax.inject.Inject

@Composable
fun TopLeftBar(cameraScreenViewModel: CameraScreenViewModel) {
    val currentFlatNumber = remember {cameraScreenViewModel.currentFlatNumber}
    val isFlatLocked = remember {cameraScreenViewModel.isFlatLocked}
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        FlatLabel(Modifier, "Квартира ${currentFlatNumber.value}")
        val lockClick = {
            isFlatLocked.value = !isFlatLocked.value
        }
        Crossfade(targetState = isFlatLocked.value) {
            when (it) {
                false -> FlatLock(
                    Modifier.padding(start = 8.dp),
                    onItemClick = lockClick, isLocked = IsLocked.NotLocked
                )

                true -> FlatLock(
                    Modifier.padding(start = 8.dp),
                    onItemClick = lockClick, isLocked = IsLocked.Locked
                )
            }
        }
        FlatProgress(Modifier.padding(start=8.dp),"0%")
    }
}

@Preview
@Composable
fun ChangeFlatButtonPreview() {
    ChangeFlatButton()
}

@Preview
@Composable
fun FlatLabelPreview() {
    FlatLabel(Modifier, "Квартира 128")
}

@Preview
@Composable
fun FlatLockPreview() {
    FlatLock()
}