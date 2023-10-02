package com.eyre.parentemailhelper.util;

import static android.provider.CalendarContract.ACCOUNT_TYPE_LOCAL;

import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncAdapterType;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;

import androidx.annotation.ColorInt;

import com.eyre.parentemailhelper.pojo.CalenderEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarUtil {

    private static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,                  // 2
//            CalendarContract.Calendars.OWNER_ACCOUNT,                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;

    public static Long getCalendarId(Context context, String calendarName) {

        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Cursor cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            if(cur.getString(PROJECTION_DISPLAY_NAME_INDEX).equalsIgnoreCase(calendarName)){
                return cur.getLong(PROJECTION_ID_INDEX);
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, calendarName);
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE_LOCAL);
        contentValues.put(CalendarContract.Calendars.NAME, calendarName);
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarName);
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, calendarName);
        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        contentValues.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        contentValues.put(CalendarContract.Calendars.VISIBLE, 1);
        contentValues.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "METHOD_ALERT, METHOD_EMAIL, METHOD_ALARM");
        contentValues.put(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES, "TYPE_OPTIONAL, TYPE_REQUIRED, TYPE_RESOURCE");
        contentValues.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, "AVAILABILITY_BUSY, AVAILABILITY_FREE, AVAILABILITY_TENTATIVE");

        uri = uri.buildUpon().appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, calendarName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE_LOCAL).build();
        Uri calendarURI = cr.insert(uri, contentValues);
        System.out.println(calendarURI.toString());
        return getCalendarId(context,calendarName);
    }

//    public static void updateNullAccount(Context context) {
//        ContentResolver cr = context.getContentResolver();
//        Uri uri = CalendarContract.Calendars.CONTENT_URI;
//        Cursor cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
//        while (cur.moveToNext()) {
////            System.out.println(cur.getString(PROJECTION_DISPLAY_NAME_INDEX) + " " + cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX));
//            if(cur.getString(PROJECTION_DISPLAY_NAME_INDEX).equalsIgnoreCase(TAPESTRY)){
//                uri = uri.buildUpon()
//                        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,"true")
//                        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, TAPESTRY)
//                        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, null).build();
//                ContentValues values = new ContentValues();
//                values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
//                cr.update(uri, values, "_id=8", null);
//            }
//        }
//
//    }

    public static void deleteCalendar(Context context, String calendarName) {

        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Cursor cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
//            System.out.println(cur.getString(PROJECTION_DISPLAY_NAME_INDEX) + " " + cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX));
            if(cur.getString(PROJECTION_DISPLAY_NAME_INDEX).equalsIgnoreCase(calendarName)){
                cr.delete(uri, "_id=?", new String[] {Long.toString(cur.getLong(PROJECTION_ID_INDEX))});
            }
        }
    }

//    private static final String[] EVENT_PROJECTION_2 = new String[] {
//            CalendarContract.Calendars._ID,                           // 0
//            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
//            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
//            CalendarContract.Calendars.OWNER_ACCOUNT,                 // 3
//            CalendarContract.Calendars.CALENDAR_COLOR                 // 4
//    };
////
////    // The indices for the projection array above.
////    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
////    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
////    private static final int PROJECTION_COLOR_INDEX = 4;

//    public static void getAccountCalendars(Context context) {
//        // Run query
//        Cursor cur;
//        ContentResolver cr = context.getContentResolver();
//        Uri uri = CalendarContract.Calendars.CONTENT_URI;
//        String selection = "(" + CalendarContract.Calendars.VISIBLE + " = ?)";
//        String[] selectionArgs = new String[] {"1"};
//        // Submit the query and get a Cursor object back.
//        cur = cr.query(uri, EVENT_PROJECTION_2, selection, selectionArgs, null);
//        // Use the cursor to step through the returned records
//        while (cur.moveToNext()) {
//            long calID = 0;
//            String displayName = null;
//            String accountName = null;
//            String ownerName = null;
//            @ColorInt int color = 0;
//
//            // Get the field values
//            calID = cur.getLong(PROJECTION_ID_INDEX);
//            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
//            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
//            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
//            color = cur.getInt(PROJECTION_COLOR_INDEX);
//
//            System.out.println("calID: " + calID +  " displayName: " + displayName +  " accountName: " + accountName +  " ownerName: " + ownerName +  " color: " + color);
//        }
//    }

    public static long addEventToCalendar(Context context, CalenderEvent calenderEvent,
                                          boolean isRemind, boolean isMailService) {
        ContentResolver cr = context.getContentResolver();
        String eventUriStr = "content://com.android.calendar/events";
        ContentValues event = new ContentValues();

        event.put(CalendarContract.Events.CALENDAR_ID, getCalendarId(context, TAPESTRY));
        event.put(CalendarContract.Events.TITLE, calenderEvent.getChildName() + "\nFrom: " + calenderEvent.getApprovedBy() + "\n" + calenderEvent.getTitle().trim());
        event.put(CalendarContract.Events.DESCRIPTION, calenderEvent.getContent() + " Go here to find out more: " + calenderEvent.getUrl());
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calenderEvent.getDatePlanned().getYear());
        calendar.set(Calendar.MONTH, calenderEvent.getDatePlanned().getMonthValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calenderEvent.getDatePlanned().getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));
        event.put(CalendarContract.Events.DTSTART, calendar.getTimeInMillis());
        event.put(CalendarContract.Events.DTEND, calendar.getTimeInMillis() + (1000 * 60 * 60));
        event.put(CalendarContract.Events.HAS_ALARM, 1);

        Uri eventUri = cr.insert(Uri.parse(eventUriStr), event);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (isRemind) {
            int method = 1;
            int lastReminder = 40;
            int firstReminder = 14*60;
            addAlarms(cr, eventID, lastReminder, method);
            addAlarms(cr, eventID, firstReminder, method);
            //        String reminderUriString = "content://com.android.calendar/reminders";
            //        ContentValues reminderValues = new ContentValues();
            //        reminderValues.put(Reminders.EVENT_ID, eventID);
            //        // Default value of the system. Minutes is a integer
            //        reminderValues.put(Reminders.MINUTES, 15);
            //        // Alert Methods: Default(0), Alert(1), Email(2), SMS(3)
            //        reminderValues.put(Reminders.METHOD,Reminders.METHOD_ALARM);
            //
            //        cr.insert(Uri.parse(reminderUriString), reminderValues);
        }
        if (isMailService) {

            String attendeeName = "Rick";
            String attendeeEmail = "rbarnes23@gmail.com";
            int attendeeRelationship = 2;
            int attendeeType = 2;
            int attendeeStatus = 1;
            addAttendees(cr, eventID, attendeeName, attendeeEmail,
                    attendeeRelationship, attendeeType, attendeeStatus);
            attendeeName = "Marion";
            attendeeEmail = "marion.a.barnes@gmail.com";
            attendeeRelationship = 4;
            attendeeType = 2;
            attendeeStatus = 3;
            addAttendees(cr, eventID, attendeeName, attendeeEmail,
                    attendeeRelationship, attendeeType, attendeeStatus);

        }

        return eventID;
    }

    private static void addAlarms(ContentResolver cr, long eventId,
                                  int minutes, int method) {
        String reminderUriString = "content://com.android.calendar/reminders";
        ContentValues reminderValues = new ContentValues();
        reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventId);
        // Default value of the system. Minutes is a integer
        reminderValues.put(CalendarContract.Reminders.MINUTES, minutes);
        // Alert Methods: Default(0), Alert(1), Email(2), SMS(3)
        reminderValues.put(CalendarContract.Reminders.METHOD, method);

        cr.insert(Uri.parse(reminderUriString), reminderValues);

    }

    private static void addAttendees(ContentResolver cr, long eventId,
                                     String attendeeName, String attendeeEmail,
                                     int attendeeRelationship, int attendeeType, int attendeeStatus) {
        String attendeuesesUriString = "content://com.android.calendar/attendees";
        /********* To add multiple attendees need to insert ContentValues multiple times ***********/
        ContentValues attendeesValues = new ContentValues();
        attendeesValues.put(CalendarContract.Attendees.EVENT_ID, eventId);
        // Attendees name
        attendeesValues.put(CalendarContract.Attendees.ATTENDEE_NAME,
                attendeeName);
        // Attendee email
        attendeesValues.put(CalendarContract.Attendees.ATTENDEE_EMAIL,
                attendeeEmail);
        // ship_Attendee(1), Relationship_None(0), Organizer(2), Performer(3), Speaker(4)
        attendeesValues.put(
                CalendarContract.Attendees.ATTENDEE_RELATIONSHIP,
                attendeeRelationship);
        // None(0), Optional(1), Required(2), Resource(3)
        attendeesValues.put(CalendarContract.Attendees.ATTENDEE_TYPE,
                attendeeType);
        // None(0), Accepted(1), Decline(2), Invited(3), Tentative(4)
        attendeesValues.put(CalendarContract.Attendees.ATTENDEE_STATUS,
                attendeeStatus);
        cr.insert(Uri.parse(attendeuesesUriString), attendeesValues); //Uri attendeuesesUri =

    }
}
