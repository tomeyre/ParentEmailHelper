package com.eyre.parentemailhelper.asyncTask;

import android.os.Handler;
import android.os.Looper;

import com.eyre.parentemailhelper.util.GetRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TapestryCredentials {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    public void check(String username, String password) {

        executor.execute(new Runnable() {
             @Override
             public void run() {

                 String json = new GetRequest().getJSONFromUrl("https://tapestryjournal.com/", null, null);

                 System.out.println(json);

                 handler.post(new Runnable() {
                     @Override
                     public void run() {
                         //UI Thread work here
                     }
                 });
             }
         });
    }
}
