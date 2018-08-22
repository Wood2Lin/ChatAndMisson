package com.coreimagine.chatandmisson.interfaces;

import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;

/**
 * Created by Woodlin on 2018/4/8.
 */

public interface HttpCallBack{
    void onSuccess(JSONObject result);

//    void onError(Throwable ex, boolean isOnCallback);
//
//    void onCancelled(Callback.CancelledException cex);
//
//    void onFinished();

}

