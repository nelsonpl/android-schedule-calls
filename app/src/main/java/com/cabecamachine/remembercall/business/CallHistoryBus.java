package com.cabecamachine.remembercall.business;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract.Contacts;

import com.cabecamachine.remembercall.entities.CallHistory;
import com.cabecamachine.remembercall.entities.Contact;
import com.cabecamachine.remembercall.entities.Phone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CallHistoryBus {

    private Context context;

    public CallHistoryBus(Context context) {
        this.context = context;
    }

    public List<CallHistory> list() {

        Calendar fiveDayBefore = Calendar.getInstance();
        fiveDayBefore.set(Calendar.HOUR_OF_DAY, -48);

        ContentResolver cr = context.getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.NUMBER, CallLog.Calls.DATE};
        String selection = String.format("%s >= ?", CallLog.Calls.DATE);
        String[] selectionArgs = new String[]{Long.toString(fiveDayBefore.getTimeInMillis())};
        String sortOrder = String.format("%s DESC", CallLog.Calls.DATE);

        Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
        List<CallHistory> list = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                CallHistory entity = new CallHistory();
                entity.setContactName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                entity.setPhoneLabel(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL)));
                entity.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                entity.setDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
                list.add(entity);
            }
        }
        cursor.close();

        return list;

    }

//    public Contact get(long id) {
//
//        ContentResolver cr = context.getContentResolver();
//
//        Uri uri = Contacts.CONTENT_URI;
//        String[] projection = new String[]{Contacts._ID, Contacts.DISPLAY_NAME};
//        String selection = Contacts._ID + " = " + id;
//
//        String[] selectionArgs = null;
//        String sortOrder = Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
//
//        Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
//
//        Contact item = new Contact();
//
//        if (cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                item.setId(id);
//                item.setName(cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME)));
//            }
//        }
//        cursor.close();
//
//        return item;
//
//    }

}
