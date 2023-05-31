package ru.mrmarvel.hellofigma.screens

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.tencent.yolov8ncnn.Yolov8Ncnn
import ru.mrmarvel.hellofigma.camerabutton.Action
import ru.mrmarvel.hellofigma.camerabutton.CameraButton
import ru.mrmarvel.hellofigma.changeroombutton.ChangeRoomButton
import ru.mrmarvel.hellofigma.data.CameraScreenViewModel
import ru.mrmarvel.hellofigma.flatinputfield.FlatInputField
import ru.mrmarvel.hellofigma.simpleroombutton.SimpleRoomButton
import ru.mrmarvel.hellofigma.ui.CameraFragment
import ru.mrmarvel.hellofigma.ui.MOPFragment
import ru.mrmarvel.hellofigma.ui.RoomFragment
import ru.mrmarvel.hellofigma.ui.TopLeftBar

//import  ru.mrmarvel.hellofigma.util.processStatistic

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    cameraViewModel: CameraScreenViewModel = hiltViewModel(),
    navigateToObserveResultScreen: () -> Unit = {}
) {
    val context = LocalContext.current
    val permissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    permissions += if (Build.VERSION.SDK_INT <= 28){
        listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }else {
        listOf(Manifest.permission.CAMERA)
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions)

    if (!permissionState.allPermissionsGranted){
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }
    if (!permissionState.allPermissionsGranted){
        permissionState.revokedPermissions.forEach {
            // Toast.makeText(context, "Нужно разрешение ${it.permission}!", Toast.LENGTH_LONG).show()
        }
    }

    val isFlatLocked = remember {cameraViewModel.isFlatLocked}
    val isFlatInputShown = remember { cameraViewModel.isFlatInputShown}
    val isMOPSelected = remember {cameraViewModel.isMOPSelected}
    val isRecordingStarted = remember {cameraViewModel.isRecordingStarted}
    val yolov8Ncnn: Yolov8Ncnn = Yolov8Ncnn();

    Surface(
        Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        //yolov8Ncnn?.changeState()
        if (permissionState.allPermissionsGranted){
            CameraFragment(yolov8Ncnn = yolov8Ncnn)
        }
        AnimatedVisibility(visible = isRecordingStarted.value) {
            AnimatedVisibility(visible = isMOPSelected.value) {
                MOPFragment(cameraScreenViewModel = cameraViewModel)
            }
            AnimatedVisibility(visible = !isMOPSelected.value) {

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    TopLeftBar(cameraScreenViewModel = cameraViewModel,
                        onChangeFlatClick = {
                            isFlatInputShown.value = !isFlatInputShown.value
                        }
                    )
                }
                AnimatedVisibility(visible = isFlatLocked.value) {
                    RoomFragment(cameraViewModel = cameraViewModel, yolov8Ncnn = yolov8Ncnn)
                }
            }
        }
        AnimatedVisibility(visible = !isMOPSelected.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedVisibility(visible = isRecordingStarted.value
                            && !isMOPSelected.value
                            && !isFlatLocked.value
                    ) {
                        SimpleRoomButton(
                            modifier = Modifier.padding(bottom = 8.dp),
                            roomName = "МОП",
                            onItemClick = {
                                isMOPSelected.value = true
                            }
                        )
                    }
                    IconButton(onClick = {}) {
                        Crossfade(targetState = isRecordingStarted) {
                            val action = when (it.value) {
                                false -> Action.START
                                true -> Action.STOP
                            }
                            CameraButton(action = action, onItemClick = {
                                if (isRecordingStarted.value) {
                                    navigateToObserveResultScreen()
                                }
                                isRecordingStarted.value = !isRecordingStarted.value
                            })
                        }
                    }
                }
            }
        }
        AnimatedVisibility(visible = isFlatInputShown.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FlatChangeWindow(cameraViewModel = cameraViewModel)
        }
    }
}

@Composable
fun FlatChangeWindow(
    cameraViewModel: CameraScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val currentFlatNumber = remember {cameraViewModel.currentFlatNumber}
    val isFlatLocked = remember {cameraViewModel.isFlatLocked}
    val isFlatInputShown = remember {cameraViewModel.isFlatInputShown}
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        val textIn = remember { mutableStateOf("") }
        FlatInputField(
            fieldItem = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    BasicTextField(
                        value = textIn.value,
                        onValueChange = {textIn.value = it},
                        singleLine = true,
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                    )
                }
            },
            onOkClick = {
                val newVal = textIn.value.toIntOrNull() ?: return@FlatInputField
                currentFlatNumber.value = newVal.toString()
                isFlatLocked.value = true
                isFlatInputShown.value = false
            })

    }
}

@Preview
@Composable
fun CameraButtonPreview() {
    Column {
        CameraButton(action = Action.START)
        CameraButton(action = Action.STOP)
    }
}

@Preview
@Composable
fun ChangeRoomButtonPreview() {
    ChangeRoomButton()
}

@Preview
@Composable
fun FlatInputFieldPreview() {
    FlatInputField(
        fieldItem = {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                BasicTextField(
                    value = "1",
                    onValueChange = {},
                    singleLine = true,
                    modifier = Modifier.align(Alignment.Center),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                )
            }
        })
}

@Preview
@Composable
fun SimpleRoomButtonPreview() {
    SimpleRoomButton(roomName = "МОП")
}