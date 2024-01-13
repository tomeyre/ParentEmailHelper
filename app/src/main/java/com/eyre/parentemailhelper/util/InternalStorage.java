package com.eyre.parentemailhelper.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InternalStorage {

    public static void write(Context context, String fileName, String body, String directory){
        File dir = new File(context.getFilesDir(), directory);
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            File file = new File(dir, fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(body);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String read(Context context, String fileName, String directory) {
        File dir = new File(context.getFilesDir(), directory);
        if(!dir.exists()){
            dir.mkdir();
        }

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(dir, fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
