package com.cabecamachine.remembercall.business;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;

import com.cabecamachine.remembercall.dataaccess.ScheduleDbHelper;
import com.cabecamachine.remembercall.entities.Contact;
import com.cabecamachine.remembercall.entities.Phone;

public class ContactBus {

    private ScheduleDbHelper dbHelper;
    private Context context;
    private PhoneBus phoneBus;

    public ContactBus(Context context) {
        this.context = context;
        dbHelper = new ScheduleDbHelper(context);
        phoneBus = new PhoneBus(context);
    }

    public List<Contact> list() {

        ContentResolver cr = context.getContentResolver();
        Uri uri = Contacts.CONTENT_URI;
        String[] projection = new String[]{Contacts._ID, Contacts.DISPLAY_NAME};
        String selection = Contacts.IN_VISIBLE_GROUP + " = '1' and " + Contacts.HAS_PHONE_NUMBER + " = '1'";

        String[] selectionArgs = null;
        String sortOrder = Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);

        List<Contact> list = new ArrayList<Contact>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Long id = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));

                List<Phone> phones = phoneBus.list(id);

                for (int i = 0; i < phones.size(); i++) {
                    Contact item = new Contact();
                    item.setId(id);
                    item.setName(name);
                    item.setPhone(phones.get(i));
                    //item.setPhoto(getPhoto(id));
                    list.add(item);
                }
            }
        }
        cursor.close();

        return list;

    }

    public List<Contact> listStarred() {

        ContentResolver cr = context.getContentResolver();
        Uri uri = Contacts.CONTENT_URI;
        String[] projection = new String[]{Contacts._ID, Contacts.DISPLAY_NAME};
        String selection = String.format("%s = '1' and %s = '1' and %s = '1'", Contacts.IN_VISIBLE_GROUP, Contacts.HAS_PHONE_NUMBER, Contacts.STARRED);

        String[] selectionArgs = null;
        String sortOrder = Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);

        List<Contact> list = new ArrayList<Contact>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Long id = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));

                List<Phone> phones = phoneBus.list(id);

                for (int i = 0; i < phones.size(); i++) {
                    Contact item = new Contact();
                    item.setId(id);
                    item.setName(name);
                    item.setPhone(phones.get(i));
                    //item.setPhoto(getPhoto(id));
                    list.add(item);
                }
            }
        }
        cursor.close();

        return list;

    }

//    public List<String> listName() {
//
//        ContentResolver cr = context.getContentResolver();
//        Uri uri = Contacts.CONTENT_URI;
//        String[] projection = new String[]{Contacts._ID, Contacts.DISPLAY_NAME};
//        String selection = Contacts.IN_VISIBLE_GROUP + " = '1' and " + Contacts.HAS_PHONE_NUMBER + " = '1'";
//
//        String[] selectionArgs = null;
//        String sortOrder = Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
//
//        Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
//
//        List<String> list = new ArrayList<String>();
//
//        if (cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//
//                Long id = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
//                String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
//
//                List<Phone> phones = phoneBus.list(id);
//
//                for (int i = 0; i < phones.size(); i++) {
//                    Contact item = new Contact();
//                    item.setId(id);
//                    item.setName(name);
//                    item.setPhone(phones.get(i));
//                    list.add(item);
//                }
//            }
//        }
//        cursor.close();
//
//        return list;
//    }

    public Contact get(long id) {

        ContentResolver cr = context.getContentResolver();

        Uri uri = Contacts.CONTENT_URI;
        String[] projection = new String[]{Contacts._ID, Contacts.DISPLAY_NAME};
        String selection = Contacts._ID + " = " + id;

        String[] selectionArgs = null;
        String sortOrder = Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);

        Contact item = new Contact();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                item.setId(id);
                item.setName(cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME)));
            }
        }
        cursor.close();

        return item;

    }

    private Bitmap getPhoto(long id) {
        Bitmap photo = null;
        InputStream inputStream = Contacts.openContactPhotoInputStream(context.getContentResolver(), ContentUris.withAppendedId(Contacts.CONTENT_URI, id));
        if (inputStream != null) {
            photo = BitmapFactory.decodeStream(inputStream);
        }
        return photo;
    }
}
