package com.happycbbboy.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.happycbbboy.R;

public class NotifyManager {
    // 在需要弹出提示框的地方调用此方法
    public static void showAlertDialog(Context context, String title, String message,DialogInterface.OnClickListener confirm,DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 使用自定义布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.alert_page, null);

        // 获取布局中的控件
        TextView titleTextView = customView.findViewById(R.id.alertTitle);
        TextView messageTextView = customView.findViewById(R.id.alertMessage);

        // 设置标题和消息
        titleTextView.setText(title);
        messageTextView.setText(message);

        // 设置按钮
        builder.setPositiveButton("取消", cancel);
        builder.setPositiveButton("确定", confirm);

        // 设置自定义布局
        builder.setView(customView);

        // 创建并显示对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
