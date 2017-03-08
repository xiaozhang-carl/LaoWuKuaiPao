package com.laowukuaipao;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * CreditUser: Yaotian Leung
 * Date: 2013-11-13
 * Time: 17:39
 * https://github.com/GrenderG/Toasty
 */
public class ToastUtil {

    private static Toast toast = null;

    public static void show(Context context, int res) {
        if (context == null) {
            return;
        }
        show(context, context.getString(res));
    }

    public static void show(Context context, String message) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//            toast.setView();
        } else {
            toast.setText(message);
        }
        //因为dialog显示的时间是100，所以这里延时显示
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        }, 100);
    }

}
