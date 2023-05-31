package ru.mrmarvel.hellofigma.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tencent.yolov8ncnn.CheckLogic
import com.tencent.yolov8ncnn.FlatStatistic
import com.tencent.yolov8ncnn.RoomType
import com.tencent.yolov8ncnn.Yolov8Ncnn
import ru.mrmarvel.hellofigma.changeflatbutton.ChangeFlatButton
import ru.mrmarvel.hellofigma.data.CameraScreenViewModel
import ru.mrmarvel.hellofigma.endroombutton.EndRoomButton
import ru.mrmarvel.hellofigma.flatinputfield.FlatInputField
import ru.mrmarvel.hellofigma.roomprogressbutton.RoomProgressButton
import ru.mrmarvel.hellofigma.simpleroombutton.SimpleRoomButton
import java.util.HashMap
import java.util.Vector

@Composable
fun RoomFragment(cameraViewModel: CameraScreenViewModel, yolov8Ncnn: Yolov8Ncnn) {
    val isFlatChangeWindowShown = remember { mutableStateOf(false) }
    val currentFlatNumber = remember {cameraViewModel.currentFlatNumber}
    val currentRoomType = remember {cameraViewModel.selectedRoomType}
    val isFlatLocked = remember {cameraViewModel.isFlatLocked}


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
                yolov8Ncnn.changeState()
                var roomType = cameraViewModel.selectedRoomType.value

                cameraViewModel.roomRealData =  yolov8Ncnn.data ?: HashMap<Int, Vector<Float>>()
                Log.d("data", cameraViewModel.roomRealData.toString())
                var flatStatistic = FlatStatistic()

                // Записываем среднюю уверенность
                // TODO: Добавить логику парного соответствия
                for ((key, value) in cameraViewModel.roomRealData) {
                    // TODO: Сделать выбор комнаты
                    when (roomType) {
                        RoomType.KITCHEN -> {
                            if (key in flatStatistic.kitchen.keys && value[1] > 20)
                                flatStatistic.kitchen[key] = value[0]
                        }
                        RoomType.LIVING -> {
                            if (key in flatStatistic.living.keys && value[1] > 20)
                                flatStatistic.living[key] = value[0]
                        }

                        RoomType.HALL -> {
                            if (key in flatStatistic.hall.keys && value[1] > 20)
                                flatStatistic.hall[key] = value[0]
                        }

                        RoomType.SANITARY -> {
                            if (key in flatStatistic.sanitary.keys && value[1] > 20)
                                flatStatistic.sanitary[key] = value[0]
                        }

                    }
                }
                // TODO: Проверить логику
                val floor_classes: IntArray = intArrayOf(5, 6)
                val ceiling_classes: IntArray = intArrayOf(1, 2)
                val wall_classes: IntArray = intArrayOf(15, 17, 18)
                Log.d("data", flatStatistic.kitchen.toString())
                when(roomType){
                    RoomType.KITCHEN -> {
                        CheckLogic.compareAndResetClasses(flatStatistic.kitchen, floor_classes)
                        CheckLogic.compareAndResetClasses(flatStatistic.kitchen, ceiling_classes)
                        CheckLogic.compareAndResetClasses(flatStatistic.kitchen, wall_classes)
                    }
                    RoomType.LIVING -> {
                        CheckLogic.compareAndResetClasses(flatStatistic.living, floor_classes)
                        CheckLogic.compareAndResetClasses(flatStatistic.living, ceiling_classes)
                        CheckLogic.compareAndResetClasses(flatStatistic.living, wall_classes)
                    }
                    RoomType.HALL -> {
                        CheckLogic.compareAndResetClasses(flatStatistic.hall, floor_classes)
                        CheckLogic.compareAndResetClasses(flatStatistic.hall, ceiling_classes)
                        CheckLogic.compareAndResetClasses(flatStatistic.hall, wall_classes)
                    }
                    RoomType.SANITARY -> {
                        CheckLogic.compareAndResetClasses(flatStatistic.sanitary, floor_classes)
                        CheckLogic.compareAndResetClasses(flatStatistic.sanitary, ceiling_classes)
                        CheckLogic.compareAndResetClasses(flatStatistic.sanitary, wall_classes)
                    }
                }
                currentRoomType.value = null
                Log.d("data", flatStatistic.kitchen.toString())
            })
        }
    }
    val roomsNames = listOf("Санузел", "Коридор", "Жилая", "Кухня")
    AnimatedVisibility(
        visible = remember { cameraViewModel.selectedRoomType}.value == null,
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
                        cameraViewModel.selectedRoomType.value = RoomType.toEnum(roomsNames[i])
                    })
                }
            }
        }
    }

}