package com.coreimagine.chatandmisson.utils;

import android.util.Log;
import android.util.Pair;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class HttpParamsUtil {

    public static ArrayList setParams(ArrayList<Pair<String,Object>> list,String first,Object second){
        list.add(new Pair<>(first, second));
        return list;
    }
}
