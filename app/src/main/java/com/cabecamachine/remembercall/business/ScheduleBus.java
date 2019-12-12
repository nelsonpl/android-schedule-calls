package com.cabecamachine.remembercall.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cabecamachine.remembercall.dataaccess.ScheduleDbHelper;
import com.cabecamachine.remembercall.entities.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleBus {
    private ScheduleDbHelper dbHelper;

    public ScheduleBus(Context context) {
        dbHelper = new ScheduleDbHelper(context);
    }

    public long save(Schedule entity) {

        long id;

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ScheduleDbHelper.TABLE_CONTACT_NAME, entity.getContactName());
        values.put(ScheduleDbHelper.TABLE_PHONE_NUMBER, entity.getPhoneNumber());
        values.put(ScheduleDbHelper.TABLE_ALARM, entity.getAlarm());
        values.put(ScheduleDbHelper.TABLE_NOTE, entity.getNote());

        if (entity.getId() == 0) {
            // Insert the new row, returning the primary key value of the new row
            id = db.insert(ScheduleDbHelper.TABLE_NAME, null, values);
        } else {
            values.put(ScheduleDbHelper.TABLE_ID, entity.getId());
            id = db.update(ScheduleDbHelper.TABLE_NAME, values, ScheduleDbHelper.TABLE_ID + " =?", new String[]{Long.toString(entity.getId())});
        }
        return id;
    }

        public boolean delete(long id) {
        long flag;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        flag = db.delete(ScheduleDbHelper.TABLE_NAME, ScheduleDbHelper.TABLE_ID + " = ?", new String[]{Long.toString(id)});

        return flag > 0;
    }
//
    public Schedule get(long id) {
        Schedule entity = null;

        // item.setContactId(1);
        // item.setContactName("Nelson Peixoto Lopes");
        //
        // item.setPhoneId(1);
        // item.setPhoneNumber("(65) 9200 0052");
        // item.setPhoneLabel("Claro");
        //
        // item.setAlarmDate(new Date());
        //
        // list.add(item);
        // list.add(item);
        // list.add(item);
        // list.add(item);
        // list.add(item);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ScheduleDbHelper.TABLE_ID,
                ScheduleDbHelper.TABLE_CONTACT_NAME,
                ScheduleDbHelper.TABLE_PHONE_NUMBER,
                ScheduleDbHelper.TABLE_ALARM,
                ScheduleDbHelper.TABLE_NOTE,
        };

        //Date now = new Date();

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = RCallDbHelper.TABLE_ALARM_DATE + " ASC";
        String selection = ScheduleDbHelper.TABLE_ID+" = ?";
        String[] selectionArgs = new String[]{Long.toString(id)};
        //selectionArgs[0] = Long.toString(now.getTime());

        Cursor cursor = db.query(
                ScheduleDbHelper.TABLE_NAME,  // The table to query
                projection,                // The columns to return
                selection,           // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,                      // don't group the rows
                null,                      // don't filter by row groups
                ""/*sortOrder*/           // The sort order
        );

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String contactName = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_CONTACT_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_PHONE_NUMBER));
                Long alarm = cursor.getLong(cursor.getColumnIndex(ScheduleDbHelper.TABLE_ALARM));
                String note = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_NOTE));

                entity = new Schedule();
                entity.setId(id);
                entity.setContactName(contactName);
                entity.setPhoneNumber(phoneNumber);
                entity.setAlarm(alarm);
                entity.setNote(note);
            }
        }
        cursor.close();

        return entity;
    }

    public List<Schedule> list() {
        List<Schedule> list = new ArrayList<>();

        // item.setContactId(1);
        // item.setContactName("Nelson Peixoto Lopes");
        //
        // item.setPhoneId(1);
        // item.setPhoneNumber("(65) 9200 0052");
        // item.setPhoneLabel("Claro");
        //
        // item.setAlarmDate(new Date());
        //
        // list.add(item);
        // list.add(item);
        // list.add(item);
        // list.add(item);
        // list.add(item);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ScheduleDbHelper.TABLE_ID,
                ScheduleDbHelper.TABLE_CONTACT_NAME,
                ScheduleDbHelper.TABLE_PHONE_NUMBER,
                ScheduleDbHelper.TABLE_ALARM,
                ScheduleDbHelper.TABLE_NOTE,
        };

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);

        // How you want the results sorted in the resulting Cursor
        String sortOrder = ScheduleDbHelper.TABLE_ALARM + " ASC";
        String selection = ScheduleDbHelper.TABLE_ALARM +" >= ?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = Long.toString(now.getTimeInMillis());

        Cursor cursor = db.query(
                ScheduleDbHelper.TABLE_NAME,    // The table to query
                projection,                     // The columns to return
                selection,                      // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                sortOrder                       // The sort order
        );

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Long id = cursor.getLong(cursor.getColumnIndex(ScheduleDbHelper.TABLE_ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_CONTACT_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_PHONE_NUMBER));
                Long alarm = cursor.getLong(cursor.getColumnIndex(ScheduleDbHelper.TABLE_ALARM));
                String note = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_NOTE));

                Schedule item = new Schedule();
                item.setId(id);
                item.setContactName(contactName);
                item.setPhoneNumber(phoneNumber);
                item.setAlarm(alarm);
                item.setNote(note);
                list.add(item);
            }
        }
        cursor.close();

        return list;
    }

    public List<Schedule> listOld() {
        List<Schedule> list = new ArrayList<>();

        // item.setContactId(1);
        // item.setContactName("Nelson Peixoto Lopes");
        //
        // item.setPhoneId(1);
        // item.setPhoneNumber("(65) 9200 0052");
        // item.setPhoneLabel("Claro");
        //
        // item.setAlarmDate(new Date());
        //
        // list.add(item);
        // list.add(item);
        // list.add(item);
        // list.add(item);
        // list.add(item);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ScheduleDbHelper.TABLE_ID,
                ScheduleDbHelper.TABLE_CONTACT_NAME,
                ScheduleDbHelper.TABLE_PHONE_NUMBER,
                ScheduleDbHelper.TABLE_ALARM,
                ScheduleDbHelper.TABLE_NOTE,
        };

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);

        // How you want the results sorted in the resulting Cursor
        String sortOrder = ScheduleDbHelper.TABLE_ALARM + " DESC";
        String selection = ScheduleDbHelper.TABLE_ALARM +" < ?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = Long.toString(now.getTimeInMillis());

        Cursor cursor = db.query(
                ScheduleDbHelper.TABLE_NAME,    // The table to query
                projection,                     // The columns to return
                selection,                      // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                sortOrder                       // The sort order
        );

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Long id = cursor.getLong(cursor.getColumnIndex(ScheduleDbHelper.TABLE_ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_CONTACT_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_PHONE_NUMBER));
                Long alarm = cursor.getLong(cursor.getColumnIndex(ScheduleDbHelper.TABLE_ALARM));
                String note = cursor.getString(cursor.getColumnIndex(ScheduleDbHelper.TABLE_NOTE));

                Schedule item = new Schedule();
                item.setId(id);
                item.setContactName(contactName);
                item.setPhoneNumber(phoneNumber);
                item.setAlarm(alarm);
                item.setNote(note);
                list.add(item);
            }
        }
        cursor.close();

        return list;
    }

}
