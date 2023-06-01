package ru.mrmarvel.hellofigma.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrmarvel.hellofigma.bluetextline.BlueTextLine
import ru.mrmarvel.hellofigma.changeflatbutton.ChangeFlatButton
import ru.mrmarvel.hellofigma.data.CameraScreenViewModel
import ru.mrmarvel.hellofigma.flatlabel.FlatLabel
import ru.mrmarvel.hellofigma.flatlock.FlatLock
import ru.mrmarvel.hellofigma.flatlock.IsLocked
import ru.mrmarvel.hellofigma.flatnumberbutton.FlatNumberButton

@Composable
fun TopLeftBar(
    cameraScreenViewModel: CameraScreenViewModel,
    modifier: Modifier = Modifier,
    onChangeFlatClick: () -> Unit = {}
) {
    val currentFlatNumber = remember {cameraScreenViewModel.currentFlatNumber}
    val isFlatLocked = remember {cameraScreenViewModel.isFlatLocked}
    val isFlatInputShown = remember {cameraScreenViewModel.isFlatInputShown}
    val selectedRoom = remember {cameraScreenViewModel.selectedRoomType}

    val elementModifier = Modifier.padding(horizontal = 4.dp)
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BlueTextLine(modifier=elementModifier, text="Квартира")
        FlatNumberButton(modifier = elementModifier, roomNumber = currentFlatNumber.value, onItemClick = {
            isFlatInputShown.value = !isFlatInputShown.value
        })
        val lockClick = {
            isFlatLocked.value = !isFlatLocked.value
            isFlatInputShown.value = false
            selectedRoom.value = null
        }
        Crossfade(targetState = isFlatLocked.value) {
            when (it) {
                false -> FlatLock(
                    elementModifier,
                    onItemClick = lockClick, isLocked = IsLocked.NotLocked
                )

                true -> FlatLock(
                    elementModifier,
                    onItemClick = lockClick, isLocked = IsLocked.Locked
                )
            }
        }
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

@Preview
@Composable
fun FlatNumberButtonPreview() {
    FlatNumberButton(roomNumber = "228")
}