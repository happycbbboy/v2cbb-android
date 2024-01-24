package com.happycbbboy.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Notify {

    // 在需要弹出提示框的地方调用此方法
    public static void showAlertDialog(Context context, String title, String message) {
        // 创建 AlertDialog.Builder 对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题和消息
        builder.setTitle(title);
        builder.setMessage(message);

        // 设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在确定按钮被点击时执行的操作
                // 如果不需要额外操作，可以将此方法留空
            }
        });

        // 设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在取消按钮被点击时执行的操作
                // 如果不需要额外操作，可以将此方法留空
            }
        });

        // 创建并显示 AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
