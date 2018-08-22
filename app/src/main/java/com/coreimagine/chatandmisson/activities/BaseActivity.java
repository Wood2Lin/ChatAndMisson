package com.coreimagine.chatandmisson.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;



public abstract class BaseActivity extends AppCompatActivity {
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    public abstract void initView();

    public <T extends View> T getView(int id){
        return findViewById(id);
    }
    public Toolbar setToolBar(String title, int id, boolean isNeedBackBtn){
        Toolbar toolbar = findViewById(id);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isNeedBackBtn);
        return toolbar;
    }
    //返回菜单  固定返回箭头写法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

