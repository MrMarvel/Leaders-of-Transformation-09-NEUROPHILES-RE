package ru.mrmarvel.hellofigma.ui

import android.util.Log
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
import com.tencent.yolov8ncnn.CheckLogic
import com.tencent.yolov8ncnn.RoomType
import com.tencent.yolov8ncnn.Yolov8Ncnn
import ru.mrmarvel.hellofigma.data.CameraScreenViewModel
import ru.mrmarvel.hellofigma.red2linebutton.Red2lineButton

fun processMOPStatistic(cameraViewModel: CameraScreenViewModel, yolov8Ncnn: Yolov8Ncnn){

    cameraViewModel.roomRealData = yolov8Ncnn.data
    cameraViewModel.floorMOPStatistic.addMOP()
    Log.d("data", cameraViewModel.roomRealData.toString())

    for ((key, value) in cameraViewModel.roomRealData) {
        if (key in cameraViewModel.floorMOPStatistic.mop.keys && value[1] > 20){
            val index = cameraViewModel.floorMOPStatistic.mop[key]?.lastIndex ?: 0
            cameraViewModel.floorMOPStatistic.mop[key]?.set(index, value[0])
        }
    }
    // TODO: Проверить логику
    val floor_classes: IntArray = intArrayOf(5, 6)
    val ceiling_classes: IntArray = intArrayOf(1, 2)
    val wall_classes: IntArray = intArrayOf(15, 17, 18)

    Log.d("data", cameraViewModel.floorMOPStatistic.mop.toString())
    //CheckLogic.compareAndResetClasses(cameraViewModel.floorMOPStatistic.mop, floor_classes)
    //CheckLogic.compareAndResetClasses(cameraViewModel.floorMOPStatistic.mop, ceiling_classes)
    //CheckLogic.compareAndResetClasses(cameraViewModel.floorMOPStatistic.mop, wall_classes)
}

@Composable
fun MOPFragment(cameraViewModel: CameraScreenViewModel, yolov8Ncnn: Yolov8Ncnn, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(32.dp),
    ) {
        var state = remember{ mutableStateOf(true)}
        yolov8Ncnn.changeState(state.value)
        Red2lineButton(Modifier.align(Alignment.BottomCenter), "Завершить", "МОП",
            onItemClick = {
                state.value = false
                cameraViewModel.isMOPSelected.value = false
                processMOPStatistic(cameraViewModel, yolov8Ncnn)
            }
        )
    }
}

@Preview
@Composable
fun Red2LineButtonPreview() {
    Red2lineButton(line1 = "Завершить", line2 = "МОП")
}