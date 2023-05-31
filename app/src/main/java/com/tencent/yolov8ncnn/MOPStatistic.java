package com.tencent.yolov8ncnn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class MOPStatistic {

    public static List<Integer> mop_map = Arrays.asList(3, 8, 9, 13, 16);
    public HashMap<Integer, Vector<Float>> mop = new HashMap<Integer, Vector<Float>>();
}
