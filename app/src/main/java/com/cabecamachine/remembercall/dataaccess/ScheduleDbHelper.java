package com.cabecamachine.remembercall.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDbHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ScheduleDb.db";

    public static final String TABLE_NAME = "SCHEDULE_CALL";
    public static final String TABLE_ID = "ID";
    public static final String TABLE_CONTACT_NAME = "CONTACT_NAME";
    public static final String TABLE_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String TABLE_ALARM = "ALARM";
    public static final String TABLE_NOTE = "NOTE";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE '" + TABLE_NAME + "' ("
                    + "'" + TABLE_ID + "' INTEGER NOT NULL, "
                    + "'" + TABLE_CONTACT_NAME + "' TEXT, "
                    + "'" + TABLE_PHONE_NUMBER + "' TEXT NOT NULL, "
                    + "'" + TABLE_ALARM + "' INTEGER NOT NULL, "
                    + "'" + TABLE_NOTE + "' TEXT, PRIMARY KEY(" + TABLE_ID + ") );";

    public ScheduleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create data base, tables
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
