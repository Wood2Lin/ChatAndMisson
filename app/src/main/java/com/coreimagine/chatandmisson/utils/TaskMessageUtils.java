package com.coreimagine.chatandmisson.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreimagine.chatandmisson.App;
import com.coreimagine.chatandmisson.beans.TaskBean;
import com.vondear.rxtool.view.RxToast;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.emitter.Emitter;

public class TaskMessageUtils {

    /**
     * 编号/操作/数字
     */
    public static void performanceCommon(String msg) {
        String id = "", num = "", type = "", num2 = "", event_name = "performanceOne";
        String[] commands = msg.split("/");
        if (commands.length!=3){
            Log.e("performanceCommon: ", msg);
            if (commands[0].equals("离开")){
                type = commands[0];
                num = commands[1];
                event_name = "applyLeave";
            }else if (commands[0].equals("回归")){
                type = commands[0];
                num = commands[1];
                event_name = "applyBack";
            }else if (commands[0].equals("查")){
                type = commands[0];
                event_name = "query";
            }else if (commands[0].equals("取消")){
                type = commands[0];
                event_name = "cancel";
            }else {
                RxToast.error("输入格式不对");
                return;
            }
        }
        if (commands.length == 3) {
            if (TextUtils.isDigitsOnly(commands[0])) {
                id = commands[0];
                if (TextUtils.isDigitsOnly(commands[1])) {
                    type = "0x05";
                    num = commands[1];
                    num2 = commands[2];
                    event_name = "performanceThree";
                } else {
                    type = commands[1];
                    num = commands[2];
                }
            } else {
                if (commands[0].equals("总")){
                    if (TextUtils.isDigitsOnly(commands[1])){
                        type = commands[0];
                        num = commands[1];
                        num2 = commands[2];
                        event_name = "performanceFive";
                    }else {
                        type = commands[0];
                        num = commands[1];
                        num2 = commands[2];
                        event_name = "performanceFour";
                    }
                }else {
                    type = commands[0];
                    num = commands[1];
                    num2 = commands[2];
                    event_name = "performanceTwo";
                }
            }
        }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", App.getUserInfo().getId());
            jsonObject.put("groupName", App.groupName);
            jsonObject.put("userName", App.getUserInfo().getName());
            jsonObject.put("type", type);
            jsonObject.put("Num", id);
            jsonObject.put("msg", msg);
            jsonObject.put("flag", "");
            jsonObject.put("value1", num);
            jsonObject.put("value2", num2);
            jsonObject.put("time", simpleDateFormat.format(date));
            String timeStr = simpleDateFormat.format(date).trim().replace(" ","");
            jsonObject.put("taskID", System.currentTimeMillis());
            App.getSocket().emit(event_name, jsonObject.toString());
    }

    /**
     * 编号/操作/数字
     */
    public static void performanceOne(String id,String type,String ownNum){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID",App.getUserInfo().getId());
        jsonObject.put("type",type);
        jsonObject.put("Num",id);
        jsonObject.put("flag","");
        jsonObject.put("value1",ownNum);
        jsonObject.put("value2","");
        jsonObject.put("timw",simpleDateFormat.format(date));
        jsonObject.put("taskID",App.getUserInfo().getId()+"_"+simpleDateFormat.format(date));
        App.getSocket().emit("performanceOne", jsonObject.toString());
    }

     /**
     * 操作/次数/数字
     */
    public static void performanceTwo(String type,String ownNum,String times){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID",App.getUserInfo().getId());
        jsonObject.put("type",type);
        jsonObject.put("Num","");
        jsonObject.put("flag","0");
        jsonObject.put("value1",times);
        jsonObject.put("value2",ownNum);
        jsonObject.put("time",simpleDateFormat.format(date));
        jsonObject.put("taskID",App.getUserInfo().getId()+"_"+simpleDateFormat.format(date));
        App.getSocket().emit("performanceTwo", jsonObject.toString());
    }
 /**
     * 编号/数字/数字
     */
    public static void performanceThree(String type,String giveNum,String ownNum,String id){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID",App.getUserInfo().getId());
        jsonObject.put("type","0x05");
        jsonObject.put("Num",id);
        jsonObject.put("flag","0");
        jsonObject.put("value1",giveNum);
        jsonObject.put("value2",ownNum);
        jsonObject.put("time",simpleDateFormat.format(date));
        jsonObject.put("taskID",App.getUserInfo().getId()+"_"+simpleDateFormat.format(date));
        App.getSocket().emit("performanceThree", jsonObject.toString());
    }

    /**
     * 申请离开
     */
    public static void applyLeave(String minute){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID",App.getUserInfo().getId());
        jsonObject.put("type","0x01");
        jsonObject.put("Num","");
        jsonObject.put("flag","0");
        jsonObject.put("value1",minute);
        jsonObject.put("value2","");
        jsonObject.put("time",simpleDateFormat.format(date));
        jsonObject.put("taskID",App.getUserInfo().getId()+"_"+simpleDateFormat.format(date));
        App.getSocket().emit("applyLeave", jsonObject.toString());
    }
 /**
     * 申请回归
     */
    public static void handleRequest(TaskBean taskBean,int result){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("adminID",App.getUserInfo().getId());
        jsonObject.put("adminName",App.getUserInfo().getName());
        jsonObject.put("task", taskBean);
        jsonObject.put("result", result);
        jsonObject.put("groupName", App.groupName);
        jsonObject.put("time",simpleDateFormat.format(date));
        App.getSocket().emit("handleRequest", jsonObject.toString());
    }
}
