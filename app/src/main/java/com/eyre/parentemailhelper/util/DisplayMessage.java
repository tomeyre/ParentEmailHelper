package com.eyre.parentemailhelper.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class DisplayMessage {

    public static void displayLong(String message, Context context){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void displayShort(String message, Context context){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
