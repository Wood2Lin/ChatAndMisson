package com.coreimagine.chatandmisson.beans;

public class Message {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_Request = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_MESSAGE_MYSELF = 3;

    private int mType;
    private String mMessage;
    private String mUsername;
    private String time;

    private TaskBean taskBean;
    private RequestBean requestBean;
    private Message() {}

    public RequestBean getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(RequestBean requestBean) {
        this.requestBean = requestBean;
    }

    public TaskBean getTaskBean() {
        return taskBean;
    }

    public void setTaskBean(TaskBean taskBean) {
        this.taskBean = taskBean;
    }

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private TaskBean bean;
        private RequestBean requestBean;
        private String time;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }
        public Builder time(String time) {
            this.time = time;
            return this;
        }
        public Builder taskBean(TaskBean bean) {
            this.bean= bean;
            return this;
        }
public Builder requestBean(RequestBean bean) {
            this.requestBean= bean;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.taskBean = bean;
            message.requestBean = requestBean;
            return message;
        }
    }
}
