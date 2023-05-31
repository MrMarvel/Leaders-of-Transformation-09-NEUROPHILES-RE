package com.tencent.yolov8ncnn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class FlatStatistic {

    public static HashMap<Integer, Vector<Float>> init_map(List ignores){
        HashMap<Integer, Vector<Float>> map = new HashMap<Integer, Vector<Float>>();
        for (int i = 0; i < 21; i++){
            if (!ignores.contains(i))
                map.put(i, new Vector<Float>());
        }
        return map;
    };

    public static List<Integer> kitchen_map = Arrays.asList(0, 9, 13, 16);
    public static List<Integer> living_map = Arrays.asList(0, 3, 9, 13, 16);
    public static List<Integer> hall_map = Arrays.asList(0, 3, 8, 9, 10, 13, 16, 19);
    public static List<Integer> sanitary_map = Arrays.asList(3, 8, 10, 16, 19);

    public HashMap<Integer, Vector<Float>> kitchen = new HashMap<Integer, Vector<Float>>();
    public HashMap<Integer, Vector<Float>> living = new HashMap<Integer, Vector<Float>>();
    public HashMap<Integer, Vector<Float>> hall = new HashMap<Integer, Vector<Float>>();
    public HashMap<Integer, Vector<Float>> sanitary = new HashMap<Integer, Vector<Float>>();
}