package com.coreimagine.chatandmisson;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.coreimagine.chatandmisson.beans.UserInfo;
import com.tencent.bugly.crashreport.CrashReport;
import com.vondear.rxtool.RxTool;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class App extends Application {
    private static Socket mSocket;
    private static SharedPreferences sharedPreferences;
    public static DbManager dbManager;
    public static String groupName;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        RxTool.init(this);
        CrashReport.initCrashReport(getApplicationContext(), "08ec3a7647", false);
        try {
            mSocket = IO.socket("http://47.100.118.90:60207");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        sharedPreferences = getSharedPreferences("USER_INFO",MODE_PRIVATE);
        initDb();
    }
    public static Socket getSocket() {
        return mSocket;
    }

    public static void saveUserInfo(String user_info){
        sharedPreferences.edit().putString("user_info",user_info).commit();
    }

    public static UserInfo getUserInfo(){
        UserInfo userInfo = null;
        if (!TextUtils.isEmpty(sharedPreferences.getString("user_info",""))){
            userInfo = JSON.toJavaObject(JSON.parseObject(sharedPreferences.getString("user_info","")),UserInfo.class);
        }
        return userInfo;
    }

    public static void Logout(){
        sharedPreferences.edit().putString("user_info","").commit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mSocket.connected())
            mSocket.disconnect();
    }

    protected void initDb(){
        //本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("MessageDB") //设置数据库名
                .setDbVersion(1) //设置数据库版本,每次启动应用时将会检查该版本号,
                //发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true)//设置是否开启事务,默认为false关闭事务
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {

                    }
                })//设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //balabala...
                    }
                });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        //.setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下
        dbManager = x.getDb(daoConfig);
    }


}
