package com.coreimagine.chatandmisson;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreimagine.chatandmisson.activities.MainActivity;
import com.coreimagine.chatandmisson.beans.MessageBean;
import com.coreimagine.chatandmisson.beans.RequestBean;
import com.coreimagine.chatandmisson.beans.TaskBean;

import org.xutils.ex.DbException;

import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.coreimagine.chatandmisson.App.dbManager;
import static com.coreimagine.chatandmisson.App.getUserInfo;

public class SocketService extends Service {
    private Socket mSocket;
    //    private Callback callback;
    private boolean isConnected;
    private int notificationId=0;
    private Uri uri;
    private Ringtone rt;
    private Timer timer;
    private TimerTask timerTask;
    private StopReceiverReceiver stopReceiverReceiver;
    private Thread thread;
    private boolean isInterrupt;

    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initSocket();
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stopReceiverReceiver);
//        mSocket.disconnect();
//        startService(new Intent(this,SocketService.class));
    }
    void initSocket(){
        soundThread();
        stopReceiverReceiver = new StopReceiverReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("STOP_NOTIFY_ACTION");   //为BroadcastReceiver指定action，使之用于接收同action的广播
        registerReceiver(stopReceiverReceiver,intentFilter);
        mSocket = App.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(App.groupName, onNewMessage);
        mSocket.on(App.groupName+"handleRequest", handledRequest);
        mSocket.on(App.groupName+"query", query);
        mSocket.on("SystemBroadcast", SystemBroadcast);
        mSocket.disconnect();
        mSocket.connect();

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject jsonObject = JSON.parseObject(args[0].toString());
            isInterrupt = false;
            TaskBean taskBean = JSON.toJavaObject(jsonObject,TaskBean.class);
//            boolean isSaved = isSaveMsg(taskBean);
            Intent counterIntent = new Intent();
            counterIntent.putExtra("data", args[0].toString());
            counterIntent.putExtra("event", App.groupName);
            counterIntent.putExtra("type", 0);
            counterIntent.setAction("MESSAGE_ACTION");
            sendBroadcast(counterIntent);
            if (getUserInfo().getUsertype()==1) {
                if (taskBean.getType().equals("离开") || taskBean.getType().equals("回归")) {
                    sendNotification("申请人:" + taskBean.getUserName(), "申请内容:" + taskBean.getType() + taskBean.getValue1() + "分钟");
                    if (!isInterrupt) thread.start();
                }
            }
        }
    };
    /**
     * 处理请求
     */
    private Emitter.Listener handledRequest = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("callreq: ", args[0].toString());
            Intent counterIntent = new Intent();
            counterIntent.putExtra("data", args[0].toString());
            counterIntent.putExtra("event", App.groupName+"handleRequest");
            counterIntent.putExtra("type", 1);
            counterIntent.setAction("MESSAGE_ACTION");
            sendBroadcast(counterIntent);
        }
    };
    /**
     * 查询
     */
    private Emitter.Listener query = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject jsonObject = JSON.parseObject(args[0].toString());
            Intent counterIntent = new Intent();
            counterIntent.putExtra("data", args[0].toString());
            counterIntent.putExtra("event", App.groupName+"query");
            counterIntent.putExtra("type", 2);
            counterIntent.setAction("MESSAGE_ACTION");
            sendBroadcast(counterIntent);
        }
    };
    /**
     * 系统广播
     */
    private Emitter.Listener SystemBroadcast = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject jsonObject = JSON.parseObject(args[0].toString());
            Log.e("call: ", jsonObject.toString());
            Intent counterIntent = new Intent();
            counterIntent.putExtra("data", args[0].toString());
            counterIntent.putExtra("event", App.groupName+"query");
            counterIntent.putExtra("type", 2);
            counterIntent.setAction("MESSAGE_ACTION");
            sendBroadcast(counterIntent);
        }
    };

    private boolean isSaveMsg(Object messageBean){
        try {
            return dbManager.saveBindingId(messageBean);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("call: ", "链接错误");
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = false;
            Log.e("call: ", "断开连接");
        }
    };


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(!isConnected) {
                Log.e("call: ","连接成功" );
                mSocket.emit("add user", App.getUserInfo().getName());
                isConnected = true;
            }
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = JSON.parseObject( args[0].toString());
            String username;
            username = data.getString("user_name");
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = JSON.parseObject( args[0].toString());
            String username;
            username = data.getString("user_name");
        }
    };

    @TargetApi(Build.VERSION_CODES.O)
    private void sendNotification(String title, String content){
        /**
         *  创建通知栏管理工具
         */

        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);

        /**
         *  实例化通知栏构造器
         */
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"1");

        /**
         *  设置Builder
         */
        //设置标题
        mBuilder.setContentTitle(title)
                //设置内容
                .setContentText(content)
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.alert_icon))
                //设置小图标
                .setSmallIcon(R.mipmap.alert_icon_small)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(notificationId, mBuilder.build());
        notificationId++;
    }

    private void play(){
        if (!rt.isPlaying()) rt.play();
    }

    class StopReceiverReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
//            thread.interrupt();
            thread.interrupt();
            isInterrupt = true;
        }
    }

    private void soundThread(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isInterrupt) {
                    play();
                    try {
                        thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
