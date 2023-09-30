package com.eyre.parentemailhelper.util;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eyre.parentemailhelper.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Permissions {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public static boolean checkAndRequestPermissions(Context context) {
        int readCalendar = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR);
        int writeCalendar = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_CALENDAR);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (readCalendar != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CALENDAR);
        }
        if (writeCalendar != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_CALENDAR);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions((MainActivity)context,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
