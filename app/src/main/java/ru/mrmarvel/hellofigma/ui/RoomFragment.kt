package ru.mrmarvel.hellofigma.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tencent.yolov8ncnn.CheckLogic
import com.tencent.yolov8ncnn.RoomType
import com.tencent.yolov8ncnn.Yolov8Ncnn
import ru.mrmarvel.hellofigma.data.CameraScreenViewModel
import ru.mrmarvel.hellofigma.endroombutton.EndRoomButton
import ru.mrmarvel.hellofigma.simpleroombutton.SimpleRoomButton
import com.tencent.yolov8ncnn.FlatStatistic

fun processRoomStatistic(cameraViewModel: CameraScreenViewModel, yolov8Ncnn: Yolov8Ncnn){
    val roomType = cameraViewModel.selectedRoomType.value
    cameraViewModel.roomRealData = yolov8Ncnn.data
    Log.d("data", cameraViewModel.roomRealData.toString())

    for ((key, value) in cameraViewModel.roomRealData) {
        when (roomType) {
            RoomType.KITCHEN -> {
                if (key in cameraViewModel.flatStatistic.kitchen.keys && value[1] > 20){
                    val index = cameraViewModel.flatStatistic.kitchen[key]?.lastIndex ?: 0
                    cameraViewModel.flatStatistic.kitchen[key]?.set(index, value[0])
                }
            }
            RoomType.LIVING -> {
                if (key in cameraViewModel.flatStatistic.living.keys && value[1] > 20){
                    val index = cameraViewModel.flatStatistic.living[key]?.lastIndex ?: 0
                    cameraViewModel.flatStatistic.living[key]?.set(index, value[0])
                }
            }

            RoomType.HALL -> {
                if (key in cameraViewModel.flatStatistic.hall.keys && value[1] > 20){
                    val index = cameraViewModel.flatStatistic.hall[key]?.lastIndex ?: 0
                    cameraViewModel.flatStatistic.hall[key]?.set(index, value[0])
                }
            }

            RoomType.SANITARY -> {
                if (key in cameraViewModel.flatStatistic.sanitary.keys && value[1] > 20){
                    val index = cameraViewModel.flatStatistic.sanitary[key]?.lastIndex ?: 0
                    cameraViewModel.flatStatistic.sanitary[key]?.set(index, value[0])
                }
            }

        }
    }
    // TODO: Проверить логику
    val floor_classes: IntArray = intArrayOf(5, 6)
    val ceiling_classes: IntArray = intArrayOf(1, 2)
    val wall_classes: IntArray = intArrayOf(15, 17, 18)
    Log.d("data", cameraViewModel.flatStatistic.kitchen.toString())
    when(roomType){
        RoomType.KITCHEN -> {
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.kitchen, floor_classes)
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.kitchen, ceiling_classes)
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.kitchen, wall_classes)
        }
        RoomType.LIVING -> {
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.living, floor_classes)
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.living, ceiling_classes)
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.living, wall_classes)
        }
        RoomType.HALL -> {
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.hall, floor_classes)
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.hall, ceiling_classes)
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.hall, wall_classes)
        }
        RoomType.SANITARY -> {
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.sanitary, floor_classes)
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.sanitary, ceiling_classes)
            CheckLogic.compareAndResetClasses(cameraViewModel.flatStatistic.sanitary, wall_classes)
        }
    }
    cameraViewModel.selectedRoomType.value = null
}

@Composable
fun RoomFragment(
    cameraViewModel: CameraScreenViewModel,
    yolov8Ncnn: Yolov8Ncnn,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val isFlatChangeWindowShown = remember { mutableStateOf(false) }
    val currentFlatNumber = remember {cameraViewModel.currentFlatNumber}
    val currentRoomType = remember {cameraViewModel.selectedRoomType}
    val isFlatLocked = remember {cameraViewModel.isFlatLocked}
    val isRoomStatSaved = remember {mutableStateOf(true) }


    AnimatedVisibility(visible = currentRoomType.value != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            EndRoomButton(onItemClick = {
                yolov8Ncnn.changeState(false)
                processRoomStatistic(cameraViewModel, yolov8Ncnn)
                isRoomStatSaved.value = true
            })
        }
    }
    val roomsNames = listOf("Санузел", "Коридор", "Жилая", "Кухня")
    AnimatedVisibility(
        visible = remember {cameraViewModel.selectedRoomType}.value == null,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Top),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 2.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            val elementPadding = PaddingValues(vertical = 16.dp)
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                contentPadding = elementPadding
            ) {
                items(roomsNames.size) { i ->
                    SimpleRoomButton(
                        modifier=Modifier.padding(elementPadding),
                        roomName = roomsNames[i], onItemClick = {
                            isRoomStatSaved.value = false
                            cameraViewModel.selectedRoomType.value = RoomType.toEnum(roomsNames[i])
                            cameraViewModel.flatStatistic.add_room(cameraViewModel.selectedRoomType.value)
                            yolov8Ncnn.changeState(true)
                        })
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                cameraViewModel.flatStatistic = FlatStatistic()
                Log.d("tag", "START")
            } else if (event == Lifecycle.Event.ON_STOP) {
                Log.d("tag", "STOP")
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            Log.d("tag", "STOP1")

            yolov8Ncnn.changeState(false)
            if (!isRoomStatSaved.value)
                processRoomStatistic(cameraViewModel, yolov8Ncnn)
            cameraViewModel.floorFlatStatistic.add(cameraViewModel.flatStatistic)

            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}