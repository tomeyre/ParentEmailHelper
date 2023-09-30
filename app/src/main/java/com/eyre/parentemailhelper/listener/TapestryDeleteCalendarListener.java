package com.eyre.parentemailhelper.listener;

import static com.eyre.parentemailhelper.util.CalendarUtil.deleteCalendar;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_CHECKED;
import static com.eyre.parentemailhelper.util.Permissions.checkAndRequestPermissions;

import android.content.Context;
import android.view.View;

import java.io.File;

public class TapestryDeleteCalendarListener implements View.OnClickListener {

    private Context context;

    public TapestryDeleteCalendarListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View arg0) {
        if (checkAndRequestPermissions(context)) {
            deleteCalendar(context, TAPESTRY);
            File dir = new File(context.getFilesDir(), TAPESTRY);
            File file = new File(dir, TAPESTRY_CHECKED);
            file.delete();
        }
    }


}