package ru.mrmarvel.camoletapp.screens

import android.Manifest
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.tencent.yolov8ncnn.Yolov8Ncnn
import ru.mrmarvel.camoletapp.camerabutton.Action
import ru.mrmarvel.camoletapp.camerabutton.CameraButton
import ru.mrmarvel.camoletapp.changeroombutton.ChangeRoomButton
import ru.mrmarvel.camoletapp.data.CameraScreenViewModel
import ru.mrmarvel.camoletapp.flatinputfield.FlatInputField
import ru.mrmarvel.camoletapp.simpleroombutton.SimpleRoomButton
import ru.mrmarvel.camoletapp.ui.CameraFragment
import ru.mrmarvel.camoletapp.ui.MOPFragment
import ru.mrmarvel.camoletapp.ui.RoomFragment
import ru.mrmarvel.camoletapp.ui.TopLeftBar
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

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
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.RECORD_AUDIO
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
        if (permissionState.allPermissionsGranted){
            CameraFragment(yolov8Ncnn = yolov8Ncnn)

        }
        AnimatedVisibility(visible = isRecordingStarted.value) {
            AnimatedVisibility(visible = isMOPSelected.value) {
                MOPFragment(cameraViewModel = cameraViewModel, yolov8Ncnn = yolov8Ncnn)
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
                            val lifecycleOwner = LocalLifecycleOwner.current
                            val recording: MutableState<Recording?> = remember { mutableStateOf(null)}
                            CameraButton(action = action, onItemClick = {
                                if (isRecordingStarted.value) {
                                    recording.value?.stop()
                                    recording.value = null
                                    navigateToObserveResultScreen()
                                } else {
                                    recording.value = cameraStart(
                                        context = context,
                                        lifecycleOwner = lifecycleOwner
                                    )
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

private fun cameraStart(context: Context, lifecycleOwner: LifecycleOwner): Recording? {
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val recorder = Recorder.Builder()
        .setExecutor(cameraExecutor).setQualitySelector(QualitySelector.from(Quality.LOWEST))
        .build()
    val videoCapture = VideoCapture.withOutput(recorder)
    val cameraProvider = ProcessCameraProvider.getInstance(context).get()
    try {
        // Bind use cases to camera
        cameraProvider.bindToLifecycle(
            lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, videoCapture)
    } catch(exc: Exception) {
        Log.e("MYDEBUG", "Use case binding failed", exc)
    }
    // Create MediaStoreOutputOptions for our recorder
    val FILENAME_FORMAT = "dd"
    val name = "CameraX-recording-" +
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".mp4"
    val contentValues = ContentValues().apply {
        put(MediaStore.Video.Media.DISPLAY_NAME, name)
    }
    val mediaStoreOutput = MediaStoreOutputOptions.Builder(context.contentResolver,
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        .setContentValues(contentValues)
        .build()

// 2. Configure Recorder and Start recording to the mediaStoreOutput.
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val recording =
            videoCapture.output
                .prepareRecording(context, mediaStoreOutput)
                .withAudioEnabled()
                .start(ContextCompat.getMainExecutor(context)) {
                    // Toast.makeText(context, "ТЕСТ", Toast.LENGTH_SHORT).show()
                }
        return recording
    }
    return null
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