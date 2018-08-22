package com.coreimagine.chatandmisson;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreimagine.chatandmisson.beans.MessageBean;
import com.coreimagine.chatandmisson.beans.TaskBean;

import org.xutils.ex.DbException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.coreimagine.chatandmisson.App.dbManager;

public class SocketService extends Service {
    private Socket mSocket;
//    private Callback callback;
    private boolean isConnected;

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
        initSocket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mSocket.disconnect();
//        startService(new Intent(this,SocketService.class));
    }
    void initSocket(){
        Log.e("initSocket: ", "初始化连接");
        mSocket = App.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(App.groupName, onNewMessage);
//        mSocket.on("user joined", onUserJoined);
//        mSocket.on("user out", onUserLeft);
//        mSocket.on("typing", onTyping);
//        mSocket.on("stop typing", onStopTyping);
        mSocket.disconnect();
        mSocket.connect();

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject jsonObject = JSON.parseObject(args[0].toString());
            Log.e("call: ", jsonObject.toString());
            TaskBean taskBean = JSON.toJavaObject(jsonObject,TaskBean.class);
            boolean isSaved = isSaveMsg(taskBean);
            Intent counterIntent = new Intent();
            counterIntent.putExtra("data", args[0].toString());
            counterIntent.putExtra("event", App.groupName);
            counterIntent.putExtra("saved", isSaved);
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
}
