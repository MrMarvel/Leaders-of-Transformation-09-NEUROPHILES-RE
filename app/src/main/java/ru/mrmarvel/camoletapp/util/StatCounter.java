package ru.mrmarvel.camoletapp.util;

import android.content.Context;

import androidx.compose.runtime.MutableState;

import com.tencent.yolov8ncnn.FlatStatistic;
import com.tencent.yolov8ncnn.MOPStatistic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.IntStream;

import ru.mrmarvel.camoletapp.data.CameraScreenViewModel;
import ru.mrmarvel.camoletapp.data.SharedViewModel;

public class StatCounter {

    public static HashMap<Integer, Float> flatCounter = new HashMap<>();
    public static HashMap<Integer, Float> mopCounter = new HashMap<>();


    public static void calculatePercent(Context context, SharedViewModel sharedViewModel, CameraScreenViewModel viewModel){
        List<FlatStatistic> floorFlatStatic = viewModel.getFloorFlatStatistic();
        MOPStatistic floorMOPStatic = viewModel.getFloorMOPStatistic();
        int bathConter = 0;
        MutableState<Integer> roomCounter = sharedViewModel.getObserveRoomCount();
        MutableState<Integer> MOPCounter = sharedViewModel.getObserveMOPCount();
//        int[] countableClasses = {22, 8, 11, 12, 4};
        for(FlatStatistic item: floorFlatStatic){
            // iterate through objects
            // kitchen
            for(HashMap<Integer, Vector<Float>> map: item.allMaps){
                int cnt = 0;
                for(Map.Entry<Integer, Vector<Float>> entry : map.entrySet()){
                    if (cnt == 3) {
                        bathConter+=map.get(0).size(); // count amount of bathrooms
                    }
                    cnt++;
                    int id = entry.getKey();
                    if(flatCounter.get(id) == null){
//                        if(IntStream.of(countableClasses).anyMatch(x -> x == id)){
//                            flatCounter.put(id, entry.getValue().stream().reduce(0.0f, Float::sum));
//                        }
//                        else{
//                            flatCounter.put(id, (float)entry.getValue().size());
//                        }
                        flatCounter.put(id, entry.getValue().stream().reduce(0.0f, Float::sum));
                    }
                    else{
//                        if(IntStream.of(countableClasses).anyMatch(x -> x == id)){
//                            flatCounter.put(id, flatCounter.get(id) + entry.getValue().stream().reduce(0.0f, Float::sum));
//                        }
//                        else{
//                            flatCounter.put(id, flatCounter.get(id) + entry.getValue().size());
//                        }
                        flatCounter.put(id, flatCounter.get(id) + entry.getValue().stream().reduce(0.0f, Float::sum));
                    }
                }
            }
        }

        for(Map.Entry<Integer, Vector<Float>> entry : floorMOPStatic.mop.entrySet()){
            int id = entry.getKey();
            if(mopCounter.get(id) == null){
//                if(IntStream.of(countableClasses).anyMatch(x -> x == id)){
//                    mopCounter.put(id, entry.getValue().stream().reduce(0.0f, Float::sum));
//
//                }else{
//                    mopCounter.put(id, (float)entry.getValue().size());
//                }
                mopCounter.put(id, entry.getValue().stream().reduce(0.0f, Float::sum));

            }
            else{
//                if(IntStream.of(countableClasses).anyMatch(x -> x == id)){
//                    mopCounter.put(id, mopCounter.get(id) + entry.getValue().stream().reduce(0.0f, Float::sum));
//                }
//                else{
//                    mopCounter.put(id, mopCounter.get(id) + entry.getValue().size());
//                }
                mopCounter.put(id, mopCounter.get(id) + entry.getValue().stream().reduce(0.0f, Float::sum));
            }
        }
        float[] result = new float[26];
        float countFloor = flatCounter.get(5) + flatCounter.get(20) + flatCounter.get(6);
        result[0] = flatCounter.get(6) / countFloor;
        result[1] = flatCounter.get(20) / countFloor;
        result[2] = flatCounter.get(5) / countFloor;

        float countWall = flatCounter.get(17) + flatCounter.get(15) + flatCounter.get(18);
        result[3] = flatCounter.get(18) / countWall;
        result[4] = flatCounter.get(15) / countWall;
        result[5] = flatCounter.get(17) / countWall;

        float countCeiling = flatCounter.get(2) + flatCounter.get(1) + flatCounter.get(21);
        result[6] = flatCounter.get(2) / countCeiling;
        result[7] = flatCounter.get(21) / countCeiling;
        result[8] = flatCounter.get(1) / countCeiling;

        result[9] = flatCounter.get(4) / ((float) roomCounter.getValue());
        result[10] = flatCounter.get(14) + mopCounter.get(14) > 0 ? 1.0f : 0.0f;
        result[11] = flatCounter.get(11) + mopCounter.get(11);
        result[13] = flatCounter.get(8) / flatCounter.get(22);
        result[14] = flatCounter.get(3);
        result[15] = flatCounter.get(13) / bathConter;
        result[16] = flatCounter.get(0) / bathConter;
        result[17] = flatCounter.get(9) / bathConter;


        float mFloorCounter = mopCounter.get(6) + mopCounter.get(5) + mopCounter.get(20);
        result[18] = mopCounter.get(6) / mFloorCounter;
        result[19] = mopCounter.get(20) / mFloorCounter;
        result[20] = mopCounter.get(5) / mFloorCounter;

        float mWallCounter = mopCounter.get(18) + mopCounter.get(17) + mopCounter.get(15);
        result[21] = mopCounter.get(18) / mWallCounter;
        result[22] = mopCounter.get(15) / mWallCounter;
        result[23] = mopCounter.get(17) / mWallCounter;

        float mCeilingCounter = mopCounter.get(2) + mopCounter.get(1) + mopCounter.get(21);
        result[24] = mopCounter.get(2) / mCeilingCounter;
        result[25] = mopCounter.get(21) / mCeilingCounter;
        result[26] = mopCounter.get(1) / mCeilingCounter;

        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.readWorkbook(context);
        excelWriter.fillReport(result);

    }
}
