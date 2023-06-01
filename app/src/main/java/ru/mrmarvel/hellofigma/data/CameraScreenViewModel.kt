package ru.mrmarvel.hellofigma.data

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tencent.yolov8ncnn.FlatStatistic
import com.tencent.yolov8ncnn.RoomType
import com.tencent.yolov8ncnn.Yolov8Ncnn
import ru.mrmarvel.hellofigma.domain.repository.CustomCameraRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.HashMap
import java.util.Vector
import javax.inject.Inject


@HiltViewModel
class CameraScreenViewModel @Inject constructor(
    private val repo: CustomCameraRepo
):ViewModel() {

    public val isRecordingStarted = mutableStateOf(false)
    val currentFlatNumber = mutableStateOf("128")
    val isFlatInputShown = mutableStateOf(false)
    val isFlatLocked = mutableStateOf(false)
    val selectedRoomType: MutableState<RoomType?> = mutableStateOf(null)
    val currentFlatProgress = mutableStateOf(.0f)
    val isMOPSelected = mutableStateOf(false)

    var yolov8Ncnn: MutableState<Yolov8Ncnn?> = mutableStateOf(null)
    var roomRealData = HashMap<Int, Vector<Float>>()
    var flatStatistic = FlatStatistic()
    var floorStatistic: MutableList<FlatStatistic> = mutableListOf()

    fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ){
        viewModelScope.launch {
            repo.showCameraPreview(
                previewView,
                lifecycleOwner
            )
        }
    }

    fun captureAndSave(context: Context){
        viewModelScope.launch {
            repo.captureAndSaveImage(context)
        }
    }


}