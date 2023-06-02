package ru.mrmarvel.camoletapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrmarvel.camoletapp.backbutton.BackButton
import ru.mrmarvel.camoletapp.blue1linebutton.Blue1lineButton
import ru.mrmarvel.camoletapp.camoletappbar.CamoletAppBar
import ru.mrmarvel.camoletapp.data.CameraScreenViewModel
import ru.mrmarvel.camoletapp.data.SharedViewModel
import ru.mrmarvel.camoletapp.infofield.InfoField
import ru.mrmarvel.camoletapp.util.StatCounter
import ru.mrmarvel.camoletapp.videoframe.VideoFrame

@Composable
fun ObserveResultScreen(
    cameraScreenViewModel: CameraScreenViewModel,
    sharedViewModel: SharedViewModel = SharedViewModel(),
    navigateToMonitoringScreen: () -> Unit
) {
    // var monitoringItems = listOf<MonitoringBuildingGroup>(MonitoringBuildingGroupProvider.monitoringItems[0])
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CamoletAppBar(Modifier.fillMaxWidth(),
                onBurgerClick = {
                    // Toast.makeText(context, "Открыть меню!", Toast.LENGTH_SHORT).show()
                },
                onProfileClick = {
                    // Toast.makeText(context, "Открыть профиль!", Toast.LENGTH_SHORT).show()
                },
                appBarText = "РЕЗУЛЬТАТ ОБХОДА"
            )
        },
        bottomBar = {
            val elementModifier = Modifier.padding(vertical = 8.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Blue1lineButton(elementModifier,
                    buttonText = "Скачать скор-карту",
                    onItemClicked = {
                        Log.d("MYDEBUG", "СЧИТАЕМ")
                        StatCounter.calculatePercent(context, sharedViewModel, cameraScreenViewModel)
                        Toast.makeText(context, "Таблица скачана в папку загрузок!", Toast.LENGTH_SHORT).show()
                    }
                )
                Blue1lineButton(elementModifier,
                    buttonText = "Скачать “шахматки”",
                    onItemClicked = {
                        // Toast.makeText(context, "Создать видео!", Toast.LENGTH_SHORT).show()
                        //var excelWriter: ExcelWriter = ExcelWriter()
                        //excelWriter.readWorkbook(context)
                        //excelWriter.fillReport()
                        Log.d("FILE SAVED", "12341234")
                        Toast.makeText(context, "Скачать “шахматки”!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    ) { scaffoldPadding ->
        Surface(
            Modifier.padding(scaffoldPadding),
        ) {
            ObserveResultMain(sharedViewModel, navigateToMonitoringScreen = navigateToMonitoringScreen)
        }
    }
}
@Preview
@Composable
private fun ObserveResultMain(sharedViewModel: SharedViewModel = SharedViewModel(), navigateToMonitoringScreen: () -> Unit = {}) {
    Surface(Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            BackButton(
                Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
                onItemClick = navigateToMonitoringScreen
            )
            VideoFrame()
            val elementModifier = Modifier.padding(vertical = 3.dp)
            Column(Modifier.padding(top = 24.dp)) {
                InfoField(labelText = "ЖК", valueText ="Жульен", modifier = elementModifier)
                InfoField(labelText = "Дом", valueText ="Корпус 1", modifier = elementModifier)
                InfoField(labelText = "Секция", valueText ="2", modifier = elementModifier)
                InfoField(labelText = "Этаж", valueText ="3", modifier = elementModifier)
            }
        }
    }
}

@Preview
@Composable
fun VideoFramePreview() {
    VideoFrame()
}