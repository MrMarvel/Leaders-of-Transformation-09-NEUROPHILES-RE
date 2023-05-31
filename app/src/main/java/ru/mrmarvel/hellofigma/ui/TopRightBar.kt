package ru.mrmarvel.hellofigma.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mrmarvel.hellofigma.data.CameraScreenViewModel
import ru.mrmarvel.hellofigma.flatprogress.FlatProgress
import kotlin.math.roundToInt

@Composable
fun TopRightBar(cameraScreenViewModel: CameraScreenViewModel, modifier: Modifier = Modifier) {
    val progress = remember {cameraScreenViewModel.currentFlatProgress}
    FlatProgress(modifier = modifier.padding(8.dp), progressText = "${(progress.value * 100).roundToInt()}%")

}