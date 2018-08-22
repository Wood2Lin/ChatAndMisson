package com.coreimagine.chatandmisson.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreimagine.chatandmisson.App;
import com.coreimagine.chatandmisson.R;
import com.coreimagine.chatandmisson.SocketService;
import com.coreimagine.chatandmisson.adapters.MessageAdapter;
import com.coreimagine.chatandmisson.beans.Message;
import com.coreimagine.chatandmisson.beans.MessageBean;
import com.coreimagine.chatandmisson.beans.TaskBean;
import com.coreimagine.chatandmisson.beans.UserInfo;
import com.coreimagine.chatandmisson.utils.ServiceUtil;
import com.coreimagine.chatandmisson.utils.TaskMessageUtils;
import com.vondear.rxtool.RxTextTool;
import com.vondear.rxtool.view.RxToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    private Socket mSocket = null;
    private boolean isConnected = false;
    private RecyclerView messageRecycler;
    private RecyclerView.Adapter adapter;
    private EditText message_input;
    private Button send_button;
    private List<Message> mMessages = new ArrayList<>();
    private boolean mTyping;
    private UserInfo userInfo;
    private TextView user_name_tv,login_name_tv;
    private MessageReceiver messageReceiver;
    private InputMethodManager imm;
    private DrawerLayout drawer;
    private LinearLayout linearLayout;
    private View popView;
    private String msg_append="";
    private Button btn_zong,btn_leave,btn_back,btn_refuse,btn_hang_up,btn_lai,btn_none,btn_close,btn_cancel,btn_query,btn_no,
            btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btn_xie,btn_backspace;
//    private enum MESSAGE_TYPE{
//        NORMAL,performanceOne,performanceTwo,performanceThree,applyLeave,applyBack,query,cancel,userExit
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (!ServiceUtil.isServiceStart(this,SocketService.class.getName()))
            startService(new Intent(MainActivity.this, SocketService.class));
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSocket = App.getSocket();
        messageReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MESSAGE_ACTION");   //为BroadcastReceiver指定action，使之用于接收同action的广播
        registerReceiver(messageReceiver,intentFilter);
    }

    void initView(){
        userInfo = App.getUserInfo();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(App.groupName);
        setSupportActionBar(toolbar);

        popView = findViewById(R.id.key_layout);
        btn_zong = findViewById(R.id.btn_zong);
        btn_leave = findViewById(R.id.btn_leave);
        btn_back = findViewById(R.id.btn_back);
        btn_refuse = findViewById(R.id.btn_refuse);
        btn_hang_up = findViewById(R.id.btn_hang_up);
        btn_lai = findViewById(R.id.btn_lai);
        btn_none = findViewById(R.id.btn_none);
        btn_close = findViewById(R.id.btn_close);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_query = findViewById(R.id.btn_query);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        btn8 = findViewById(R.id.btn_8);
        btn9 = findViewById(R.id.btn_9);
        btn0 = findViewById(R.id.btn_0);
        btn_xie = findViewById(R.id.btn_xie);
        btn_no = findViewById(R.id.btn_no);
        btn_backspace = findViewById(R.id.btn_backspace);

        btn_zong.setOnClickListener(this);
        btn_leave.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_refuse.setOnClickListener(this);
        btn_hang_up.setOnClickListener(this);
        btn_lai.setOnClickListener(this);
        btn_none.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_query.setOnClickListener(this);
        btn_no.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn_xie.setOnClickListener(this);
        btn_backspace.setOnClickListener(this);

        drawer = findViewById(R.id.drawer_layout);
        linearLayout = findViewById(R.id.linearLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        login_name_tv = navigationView.getHeaderView(0).findViewById(R.id.login_name_tv);
        user_name_tv = navigationView.getHeaderView(0).findViewById(R.id.user_name_tv);
        login_name_tv.setText(userInfo.getLoginname());
        user_name_tv.setText(userInfo.getName());

        adapter = new MessageAdapter(MainActivity.this, mMessages);
        messageRecycler = findViewById(R.id.messages);
        messageRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        messageRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0,10,0,10);
            }
        });
        messageRecycler.setAdapter(adapter);
        message_input = findViewById(R.id.message_input);
        message_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popView.setVisibility(View.VISIBLE);
            }
        });
        send_button = findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend(1);
            }
        });
    }

    public void onMoreClicked(View view) {
        if (!popView.isShown())
            popView.setVisibility(View.VISIBLE);
        else  popView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_backspace){
            if (TextUtils.isEmpty(msg_append)) return;
            msg_append = "";
            message_input.setText(msg_append);
            return;
        }
        Button button = (Button)view;
        msg_append += button.getText().toString();
        message_input.setText(msg_append);

//        switch (view.getId()){
//            case R.id.btn_zong:
//
//                break;
//            case R.id.btn_leave:
//
//                break;
//            case R.id.btn_back:
//
//                break;
//            case R.id.btn_refuse:
//
//                break;
//            case R.id.btn_hang_up:
//
//                break;
//            case R.id.btn_lai:
//
//                break;
//            case R.id.btn_none:
//
//                break;
//            case R.id.btn_close:
//
//                break;
//            case R.id.btn_cancel:
//
//                break;
//            case R.id.btn_query:
//
//                break;
//            case R.id.btn_no:
//
//                break;
//            case R.id.btn_1:
//
//                break;
//            case R.id.btn_2:
//
//                break;
//            case R.id.btn_3:
//
//                break;
//            case R.id.btn_4:
//
//                break;
//            case R.id.btn_5:
//
//                break;
//            case R.id.btn_6:
//
//                break;
//            case R.id.btn_7:
//
//                break;
//            case R.id.btn_8:
//
//                break;
//            case R.id.btn_9:
//
//                break;
//            case R.id.btn_0:
//
//                break;
//            case R.id.btn_xie:
//
//                break;
//
//        }
    }

    //    void initSocket(){
//        mSocket = App.getSocket();
//        mSocket.on(Socket.EVENT_CONNECT,onConnect);
//        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
//        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.on("pushMessage", onNewMessage);
////        mSocket.on("user joined", onUserJoined);
////        mSocket.on("user out", onUserLeft);
////        mSocket.on("typing", onTyping);
////        mSocket.on("stop typing", onStopTyping);
//        mSocket.connect();
//
//    }
    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.e("onReceive: ", intent.getStringExtra("data"));
            TaskBean taskBean = JSON.toJavaObject(JSON.parseObject(intent.getStringExtra("data")),TaskBean.class);
            addMessage(taskBean.getUserName(), taskBean.getMsg(),taskBean.getTime());
        }
    }

    private void attemptSend(int type) {
        if (!mSocket.connected()) return;
        mTyping = false;
//        String message = message_input.getText().toString().trim();
        if (TextUtils.isEmpty(msg_append)) {
//            message_input.requestFocus();
            return;
        }
        TaskMessageUtils.performanceCommon(msg_append);
        msg_append = "";
        message_input.setText("");
//        JSONObject jsonObject = new JSONObject();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
//
//        Date date = new Date(System.currentTimeMillis());
//        jsonObject.put("user_name",userInfo.getName());
//        jsonObject.put("login_name",userInfo.getLoginname());
//        jsonObject.put("time",simpleDateFormat.format(date));
//        jsonObject.put("type",type);
//        jsonObject.put("id",userInfo.getId());
//        jsonObject.put("group_name",App.groupName);
//        jsonObject.put("message",msg_append);
//        mSocket.emit("new message", jsonObject.toString());
    }

//    private Emitter.Listener onConnectError = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e("error", "Error connecting");
//                    Toast.makeText(MainActivity.this,
//                            "error", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    };
//    private Emitter.Listener onNewMessage = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = JSON.parseObject( args[0].toString());
//                    String username;
//                    String message;
//                    username = data.getString("user_name");
//                    message = data.getString("message");
//
////                    removeTyping(username);
//                    addMessage(username, message);
//                }
//            });
//        }
//    };

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        adapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

//    private void addParticipantsLog(int numUsers) {
//        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
//    }


    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        adapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    //    private void removeTyping(String username) {
//        for (int i = mMessages.size() - 1; i >= 0; i--) {
//            Message message = mMessages.get(i);
//            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
//                mMessages.remove(i);
//                adapter.notifyItemRemoved(i);
//            }
//        }
//    }
    private void scrollToBottom() {
        messageRecycler.scrollToPosition(adapter.getItemCount() - 1);
    }
    private void addMessage(String username, String message,String time) {
        if (this.userInfo.getName().equals(username)){
            mMessages.add(new Message.Builder(Message.TYPE_MESSAGE_MYSELF)
                    .username(username).message(message).time(time).build());
        }else {
            mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                    .username(username).message(message).time(time).build());
        }

        adapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Toast.makeText(MainActivity.this,
                            "断开连接", Toast.LENGTH_LONG).show();
                }
            });
        }
    };


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
//                        mSocket.emit("add user", userInfo.getName());
                        Toast.makeText(MainActivity.this,
                                "连接成功", Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = JSON.parseObject( args[0].toString());
                    String username;
                    username = data.getString("user_name");
                    addLog(getResources().getString(R.string.message_user_joined, username));
//                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = JSON.parseObject( args[0].toString());
                    String username;
                    username = data.getString("user_name");
                    addLog(getResources().getString(R.string.message_user_left, username));
//                    addParticipantsLog(numUsers);
//                    removeTyping(username);
                }
            });
        }
    };

//    private Emitter.Listener onTyping = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = JSON.parseObject( args[0].toString());
//                    String username;
//                    username = data.getString("username");
//                    addTyping(username);
//                }
//            });
//        }
//    };
//
//    private Emitter.Listener onStopTyping = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = JSON.parseObject( args[0].toString());
//                    String username;
//                    username = data.getString("username");
//
////                    removeTyping(username);
//                }
//            });
//        }
//    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");
        }
    };

    @Override
    public void onBackPressed() {
        drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(MainActivity.this,EditProfileActivity.class).putExtra("isModify",true));
        }
        if (id == R.id.nv_logout) {
            // Handle the camera action
            new AlertDialog.Builder(MainActivity.this).setTitle("确认退出?")
                    .setMessage("点击确认退出当前登录账号")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            App.Logout();
                            mSocket.disconnect();
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            stopService(new Intent(MainActivity.this, SocketService.class));
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
//        if (mSocket.connected())
//            mSocket.disconnect();
//        mSocket.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showPopupWindow(final Context context) {
        //设置要显示的view
        //此处可按需求为各控件设置属性
//        view.findViewById(R.id.pop1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
        PopupWindow popupWindow = new PopupWindow(popView);
        //设置弹出窗口大小
        popupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        popupWindow.setHeight(300);
        //必须设置以下两项，否则弹出窗口无法取消
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //设置动画效果
        popupWindow.setAnimationStyle(R.style.anim_menu_bottom);
        //设置显示位置,findViewById获取的是包含当前整个页面的view
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
    }

}
