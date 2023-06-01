package ru.mrmarvel.hellofigma.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.tencent.yolov8ncnn.Yolov8Ncnn
import ru.mrmarvel.hellofigma.data.CameraScreenViewModel
import ru.mrmarvel.hellofigma.red2linebutton.Red2lineButton

@Composable
fun MOPFragment(cameraScreenViewModel: CameraScreenViewModel, yolov8Ncnn: Yolov8Ncnn, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(32.dp),
    ) {
        var state = remember{ mutableStateOf(true)}
        yolov8Ncnn.changeState(state.value)
        Red2lineButton(Modifier.align(Alignment.BottomCenter), "Завершить", "МОП",
            onItemClick = {
                state.value = false
                cameraScreenViewModel.isMOPSelected.value = false
            }
        )
    }
}

@Preview
@Composable
fun Red2LineButtonPreview() {
    Red2lineButton(line1 = "Завершить", line2 = "МОП")
}