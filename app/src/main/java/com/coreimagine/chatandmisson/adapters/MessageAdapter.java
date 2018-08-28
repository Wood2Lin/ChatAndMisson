package com.coreimagine.chatandmisson.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coreimagine.chatandmisson.App;
import com.coreimagine.chatandmisson.R;
import com.coreimagine.chatandmisson.beans.Message;
import com.coreimagine.chatandmisson.beans.TaskBean;
import com.coreimagine.chatandmisson.utils.TaskMessageUtils;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int[] mUsernameColors;
    private Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        this.context = context;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        int layout = -1;
        View v = null;
        switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.item_message;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                viewHolder = new ViewHolder(v);
                break;
            case Message.TYPE_MESSAGE_MYSELF:
                layout = R.layout.item_message_myself;
                v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(layout, parent, false);
                viewHolder = new ViewHolder(v);
                break;
            case Message.TYPE_Request:
                layout = R.layout.item_request;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                viewHolder = new ViewHolderRequest(v);
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.item_request;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                viewHolder = new ViewHolderRequest(v);
                break;
        }

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Message message = mMessages.get(position);
        Log.e("onBindViewHolder: ", message.getType()+"---");
        if (message.getType() != Message.TYPE_Request) {
            viewHolder.setMessage(message.getMessage());
            viewHolder.setUsername(message.getUsername());
        }else {
            final ViewHolderRequest viewHolderRequest = (ViewHolderRequest)viewHolder;
            TaskBean taskBean = message.getTaskBean();
            if (taskBean.getType().equals("离开")){
                viewHolderRequest.request_title.setText("申请离开");
                viewHolderRequest.request_title.setBackgroundColor(context.getResources().getColor(R.color.red));
            }else {
                viewHolderRequest.request_title.setText("申请回归");
                viewHolderRequest.request_title.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
            viewHolderRequest.username.setText("用户名："+taskBean.getUserName());
            viewHolderRequest.content.setText(taskBean.getType()+" "+taskBean.getValue1()+"分钟");
            viewHolderRequest.time.setText("请求时间："+taskBean.getTime());
            if (App.getUserInfo().getUsertype()!=1){
                viewHolderRequest.btn_ll.setVisibility(View.GONE);
            }
            viewHolderRequest.allow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskMessageUtils.handleRequest(message.getTaskBean(),1);
                    viewHolderRequest.allow.setText("已允许");
                    viewHolderRequest.allow.setEnabled(false);
                    viewHolderRequest.deny.setVisibility(View.GONE);
                }
            });

            viewHolderRequest.deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskMessageUtils.handleRequest(message.getTaskBean(),0);
                    viewHolderRequest.deny.setText("已拒绝");
                    viewHolderRequest.deny.setEnabled(false);
                    viewHolderRequest.allow.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
//            mUsernameView.setTextColor(getUsernameColor(username));
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }

    }

    public class ViewHolderRequest extends ViewHolder{
        TextView request_title,username,content,time,allow,deny;
        LinearLayout btn_ll;
        public ViewHolderRequest(View itemView) {
            super(itemView);
            request_title = itemView.findViewById(R.id.request_title);
            btn_ll = itemView.findViewById(R.id.btn_ll);
            username = itemView.findViewById(R.id.username);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            allow = itemView.findViewById(R.id.allow);
            deny = itemView.findViewById(R.id.deny);
        }
    }
}
