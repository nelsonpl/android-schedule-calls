package com.cabecamachine.remembercall.business;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

import com.cabecamachine.remembercall.dataaccess.ScheduleDbHelper;
import com.cabecamachine.remembercall.entities.Contact;
import com.cabecamachine.remembercall.entities.ContactPhone;
import com.cabecamachine.remembercall.entities.Phone;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactPhoneBus {

    private ScheduleDbHelper dbHelper;
    private Context context;

    public ContactPhoneBus(Context context) {
        this.context = context;
        dbHelper = new ScheduleDbHelper(context);
    }

    public ContactPhone get(long phoneId) {

        ContactPhone entity = null;

        ContentResolver cr = context.getContentResolver();
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[]
                {
                        ContactsContract.Data._ID,
                        ContactsContract.Data.CONTACT_ID,
                        ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.LABEL
                };
        String selection = String.format("%s = ?", ContactsContract.Data._ID);
        String[] selectionArgs =  new String[]{String.valueOf(phoneId)};

        Cursor query = cr.query(uri, projection, selection, selectionArgs, null);

        if (query.getCount() > 0 && query.moveToNext()) {
            String label = (query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)));
            String number = (query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            String contactName = (query.getString(query.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)));
            Long contactId = (query.getLong(query.getColumnIndex(ContactsContract.Data.CONTACT_ID)));

            entity = new ContactPhone();
            entity.setContactId(contactId);
            entity.setContactName(contactName);
            entity.setPhoneId(phoneId);
            entity.setPhoneNumber(number);
            entity.setPhoneLabel(label);
        }
        query.close();

        return entity;
    }
}
