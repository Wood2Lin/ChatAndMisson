package com.coreimagine.chatandmisson.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Woodlin on 2018/4/8.
 */

public class DialogUtil {

    public static void showSimpleDialog(Context context,String title, String content){
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(content)
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }
}
