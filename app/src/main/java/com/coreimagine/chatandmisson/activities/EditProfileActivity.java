package com.coreimagine.chatandmisson.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreimagine.chatandmisson.App;
import com.coreimagine.chatandmisson.R;
import com.coreimagine.chatandmisson.beans.Urls;
import com.coreimagine.chatandmisson.beans.UserInfo;
import com.coreimagine.chatandmisson.interfaces.HttpCallBack;
import com.coreimagine.chatandmisson.utils.HttpParamsUtil;
import com.coreimagine.chatandmisson.utils.HttpUtil;

import java.util.ArrayList;

public class EditProfileActivity extends BaseActivity {
    private EditText name_edit,phone_edit,age_edit,intro_edit;
    boolean isModify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        isModify = getIntent().getBooleanExtra("isModify",false);
        initView();
    }

    @Override
    public void initView() {
        setToolBar("完善个人资料",R.id.toolbar,false);
        name_edit = getView(R.id.name_edit);
        phone_edit = getView(R.id.phone_edit);
        age_edit = getView(R.id.age_edit);
        intro_edit = getView(R.id.intro_edit);
        if (isModify){
            UserInfo userInfo = App.getUserInfo();
            name_edit.setText(userInfo.getName());
            name_edit.setEnabled(false);
            phone_edit.setText(userInfo.getPhone());
            age_edit.setText(userInfo.getAge()+"");
            intro_edit.setText(userInfo.getIntro());
        }
        findViewById(R.id.commit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name_edit.getText().toString())){
                    name_edit.setError("姓名不能为空");
                    return;
                }
                ArrayList<Pair<String,Object>> params = new ArrayList<>();
                HttpParamsUtil.setParams(params,"name",name_edit.getText().toString());
                HttpParamsUtil.setParams(params,"age",TextUtils.isEmpty(age_edit.getText().toString())?0:(Integer.parseInt(age_edit.getText().toString())));
                HttpParamsUtil.setParams(params,"intro",TextUtils.isEmpty(intro_edit.getText().toString())?"":intro_edit.getText().toString());
                HttpParamsUtil.setParams(params,"phone",TextUtils.isEmpty(phone_edit.getText().toString())?"":phone_edit.getText().toString());
                HttpParamsUtil.setParams(params,"sex",0);
                HttpParamsUtil.setParams(params,"avatar","");
                if (!isModify)
                    HttpParamsUtil.setParams(params,"LoginName",getIntent().getStringExtra("loginname"));
                else
                    HttpParamsUtil.setParams(params,"LoginName",App.getUserInfo().getLoginname());
                HttpUtil.postEncrype(context, Urls.modifyProfile, params, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Toast.makeText(context,result.getString("msg"),Toast.LENGTH_LONG).show();
                        if (result.getBoolean("success")){
                            App.saveUserInfo(result.getString("result"));
                            if (!isModify){
                                HttpUtil.postEncrype(context, Urls.getAllGroups, null, new HttpCallBack() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        Log.e("onSuccess: ", result.toString());
                                        if (result.getBoolean("success")){
                                            JSONArray groupsArray = JSON.parseArray(result.getString("result"));
                                            String[] groups = new String[groupsArray.size()];
                                            for (int i = 0; i < groupsArray.size(); i++) {
                                                groups[i] = groupsArray.getJSONObject(i).getString("group_name");
                                            }
                                            showGroupDialog(groups);
                                        }
                                    }
                                });
                            }else {
                                finish();
                            }
                        }
                    }
                });
            }
        });
    }
    private void showGroupDialog(final String[] groups){
        new AlertDialog.Builder(context).setTitle("请选择群组")
                .setItems(groups, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        App.getUserInfo().setGroupname(groups[i]);
                        App.groupName = groups[i];
                        App.saveUserInfo(JSON.toJSON(App.getUserInfo()).toString());
                        Log.e("onClick: ",JSON.toJSON(App.getUserInfo()).toString());
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
