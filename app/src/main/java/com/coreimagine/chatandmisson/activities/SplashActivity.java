package com.coreimagine.chatandmisson.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreimagine.chatandmisson.App;
import com.coreimagine.chatandmisson.R;
import com.coreimagine.chatandmisson.beans.Urls;
import com.coreimagine.chatandmisson.interfaces.HttpCallBack;
import com.coreimagine.chatandmisson.utils.HttpUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }
    @Override
    public void initView(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (App.getUserInfo()==null){
                    startActivity(new Intent(context,WebActivity.class));
                    finish();
//                } else {
//                    HttpUtil.postEncrype(context, Urls.getAllGroups, null, new HttpCallBack() {
//                        @Override
//                        public void onSuccess(JSONObject result) {
//                            Log.e("onSuccess: ", result.toString());
//                            if (result.getBoolean("success")){
//                                JSONArray groupsArray = JSON.parseArray(result.getString("result"));
//                                String[] groups = new String[groupsArray.size()];
//                                for (int i = 0; i < groupsArray.size(); i++) {
//                                    groups[i] = groupsArray.getJSONObject(i).getString("group_name");
//                                }
//                                showGroupDialog(groups);
//                            }
//                        }
//                    });
//                }
            }
        },1000);

    }

    private void showGroupDialog(final String[] groups){
        new AlertDialog.Builder(context).setTitle("请选择群组")
                .setItems(groups, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        App.getUserInfo().setGroupname(groups[i]);
                        App.groupName = groups[i];
                        App.saveUserInfo(JSON.toJSON(App.getUserInfo()).toString());
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                })
                .setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create().show();
    }
}
