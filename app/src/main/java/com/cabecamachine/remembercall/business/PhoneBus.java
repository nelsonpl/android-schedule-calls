package com.cabecamachine.remembercall.business;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.cabecamachine.remembercall.dataaccess.ScheduleDbHelper;
import com.cabecamachine.remembercall.entities.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneBus {

    private ScheduleDbHelper dbHelper;
    private Context context;

    public PhoneBus(Context context) {
        this.context = context;
        dbHelper = new ScheduleDbHelper(context);
    }

    public List<Phone> list(long contactId) {
        ContentResolver cr = context.getContentResolver();

        Cursor c = cr.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL},
                String.format("%s=? AND %s='%s'", ContactsContract.Data.CONTACT_ID, ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE),
                new String[]{String.valueOf(contactId)}, null);

        List<Phone> phones = new ArrayList<>();

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String label = (c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)));
                String number = (c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                Long id = (c.getLong(c.getColumnIndex(ContactsContract.Data._ID)));

                Phone phone = new Phone();
                phone.setNumber(number);
                phone.setLabel(label);
                phone.setId(id);
                phones.add(phone);
            }
        }
        c.close();

        return phones;
    }

    public Phone get(long id) {
        ContentResolver cr = context.getContentResolver();

        Cursor c = cr.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL,ContactsContract.Data.CONTACT_ID},
                String.format("%s=? AND %s='%s'", ContactsContract.Data._ID, ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE),
                new String[]{String.valueOf(id)}, null);

        Phone phone = null;

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String label = (c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)));
                String number = (c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                long contactId = (c.getLong(c.getColumnIndex(ContactsContract.Data.CONTACT_ID)));

                phone = new Phone();
                phone.setNumber(number);
                phone.setLabel(label);
                phone.setId(id);
                phone.setContactId(contactId);
            }
        }
        c.close();

        return phone;
    }

}
