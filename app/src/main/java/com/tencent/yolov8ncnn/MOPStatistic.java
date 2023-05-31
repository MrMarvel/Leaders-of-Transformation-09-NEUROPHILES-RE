package com.tencent.yolov8ncnn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class MOPStatistic {

    public HashMap<Integer, Vector<Float>> add_room(){
        for (int i = 0; i <= 21; i++){
            if (!mop_map.contains(i)) {
                if (!mop.containsKey(i)) mop.put(i, new Vector<Float>(){{add(0.0f);}});
                else mop.get(i).add(0.0f);
            }
        }
        return mop;
    };

    public static List<Integer> mop_map = Arrays.asList(3, 8, 9, 13, 16);
    public HashMap<Integer, Vector<Float>> mop = new HashMap<Integer, Vector<Float>>();
}
