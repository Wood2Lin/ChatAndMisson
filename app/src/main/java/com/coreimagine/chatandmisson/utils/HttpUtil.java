package com.coreimagine.chatandmisson.utils;

import android.content.Context;
import android.util.Pair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreimagine.chatandmisson.interfaces.HttpCallBack;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class HttpUtil {
    /**
     * 加密post请求
     * @param context
     * @param URL
//     * @param params 参数传JSONObject类型，方法内对参数进行字符串化并加密
     * @return 返回JSONObject便于解析
     */
    public static void postEncrype(final Context context, String URL, ArrayList<Pair<String,Object>> pairs, final HttpCallBack httpCallBack){
        RequestParams param = new RequestParams(URL);
        try {
            if (pairs!=null) {
                for (Pair<String, Object> pair :
                        pairs) {
                    param.addParameter(pair.first, pair.second);
                }
            }
            x.http().post(param, new Callback.CommonCallback<String>() {
                @Override

                public void onSuccess(String result) {
                    try {
                        httpCallBack.onSuccess(JSON.parseObject(result));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    DialogUtil.showSimpleDialog(context,"请求失败:",ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

