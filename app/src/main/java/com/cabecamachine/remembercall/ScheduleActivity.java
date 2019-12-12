package com.cabecamachine.remembercall;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cabecamachine.remembercall.business.CallBroadcastReceiver;
import com.cabecamachine.remembercall.business.ContactBus;
import com.cabecamachine.remembercall.business.ContactPhoneBus;
import com.cabecamachine.remembercall.business.PhoneBus;
import com.cabecamachine.remembercall.business.ScheduleBus;
import com.cabecamachine.remembercall.common.Const;
import com.cabecamachine.remembercall.common.MyDate;
import com.cabecamachine.remembercall.common.MyTime;
import com.cabecamachine.remembercall.entities.Contact;
import com.cabecamachine.remembercall.entities.Phone;
import com.cabecamachine.remembercall.entities.Schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScheduleActivity extends ActionBarActivity {

    private DatePickerFragment fAlarmDate;
    private TimePickerFragment fAlarmTime;
    private AutoCompleteTextView actContactName;
    private EditText edtPhoneNumber;
    private EditText edtNote;
    private long scheduleId;
    private ScheduleBus scheduleBus;
    private ContactPhoneBus contactPhoneBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        scheduleId = intent.getLongExtra(Const.SCHEDULE_ID, 0);

        fAlarmDate = new DatePickerFragment();
        fAlarmTime = new TimePickerFragment();
        scheduleBus = new ScheduleBus(this);
        contactPhoneBus = new ContactPhoneBus(this);

        components();
        buildViews(scheduleId);
        configAutoComplete();

    }

    //region methods

    private void buildViews(long id) {
        Intent intent;

        if (id == 0) {
            return;
        }

        Schedule r = scheduleBus.get(id);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(r.getAlarm());

        MyTime time = new MyTime();
        time.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        time.setMinute(calendar.get(Calendar.MINUTE));
        fAlarmTime.setMyTime(time);
        fAlarmTime.setEdtTime(this);

        MyDate date = new MyDate();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setMonth(calendar.get(Calendar.MONTH));
        date.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        fAlarmDate.setMyDate(date);
        fAlarmDate.setEdtDate(this);

        edtNote.setText(r.getNote());
        actContactName.setText(r.getContactName());
        edtPhoneNumber.setText(r.getPhoneNumber());

        findViewById(R.id.btnDelete).setVisibility(View.VISIBLE);
        edtPhoneNumber.clearFocus();

//        View layout = findViewById(R.id.layout);
//        layout.setFocusableInTouchMode(true);
//        layout.setFocusable(true);
//        layout.requestFocus();
    }

    private void components() {
        actContactName = (AutoCompleteTextView) findViewById(R.id.txtContactName);
        edtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        edtNote = (EditText) findViewById(R.id.txtNote);
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        private MyTime myTime;

        public MyTime getMyTime() {
            return this.myTime;
        }

        public void setMyTime(MyTime value) {
            this.myTime = value;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            //TODO: Estou acessando a activity que abriu o dialog para inserir os dados, deve haver uma forma mais elegante de fazer isto

            myTime = new MyTime();
            myTime.setHour(hour);
            myTime.setMinute(minute);

            Activity a = getActivity();
            setEdtTime(a);
        }

        private void setEdtTime(Activity a) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar time = Calendar.getInstance();
            time.set(Calendar.HOUR_OF_DAY, myTime.getHour());
            time.set(Calendar.MINUTE, myTime.getMinute());

            ((EditText) a.findViewById(R.id.txtTime)).setText(sdf.format(time.getTime()));
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private MyDate myDate;

        public MyDate getMyDate() {
            return this.myDate;
        }

        public void setMyDate(MyDate value) {
            this.myDate = value;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //TODO: Estou acessando a activity que abriu o dialog para inserir os dados, deve haver uma forma mais elegante de fazer isto

            myDate = new MyDate();
            myDate.setYear(year);
            myDate.setMonth(month);
            myDate.setDay(day);

            Activity a = getActivity();
            setEdtDate(a);
        }

        private void setEdtDate(Activity a) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar date = Calendar.getInstance();
            date.set(Calendar.DAY_OF_MONTH, myDate.getDay());
            date.set(Calendar.MONTH, myDate.getMonth());
            date.set(Calendar.YEAR, myDate.getYear());

            ((EditText) a.findViewById(R.id.txtAlarm)).setText(sdf.format(date.getTime()));
        }
    }

    private void startBroadcast(long timeStart) {

        Intent intent = new Intent(this, CallBroadcastReceiver.class);
        intent.putExtra(Const.SCHEDULE_ID, scheduleId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), (int) (scheduleId), intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeStart, pendingIntent);
    }

    private void save() {

        if (edtPhoneNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Informe um número para contato", Toast.LENGTH_SHORT).show();
            return;
        }

        MyDate myDate;
        MyTime myTime;

        myDate = fAlarmDate.getMyDate();
        myTime = fAlarmTime.getMyTime();

        if (myDate == null) {
            Toast.makeText(this, "Selecione uma data", Toast.LENGTH_SHORT).show();
            return;
        }

        if (myTime == null) {
            Toast.makeText(this, "Selecione uma hora", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, myDate.getYear());
        calendar.set(Calendar.MONTH, myDate.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, myDate.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, myTime.getHour());
        calendar.set(Calendar.MINUTE, myTime.getMinute());

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            Toast.makeText(this, "Agende para o futuro.", Toast.LENGTH_SHORT).show();
            return;
        }

        Schedule entity = new Schedule();
        entity.setId(scheduleId);
        entity.setContactName(actContactName.getText().toString());
        entity.setPhoneNumber(edtPhoneNumber.getText().toString());
        entity.setAlarm(calendar.getTimeInMillis());
        entity.setNote(edtNote.getText().toString());

        scheduleId = scheduleBus.save(entity);

        Intent intent = new Intent(this, ScheduleListActivity.class);
        intent.putExtra(Const.MESSAGE, scheduleId > 0 ? "Ligação agendada!" : "Ops, alguma coisa deu errado! :(");
        startActivity(intent);

        startBroadcast(calendar.getTimeInMillis());
        finish();
    }

    private void configAutoComplete()
    {
        String[] from =
                {
                        ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.LABEL
                };
        int[] to = {R.id.txtName, R.id.txtPhone, R.id.txtLabel};
        SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(this, R.layout.us_contact_item, null, from, to, 0);

        // Definição do filtro
        scAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                return buildCursorAutoComplete(str);
            }
        });

        // Campo que irá aparecer quando o usuário selecionar o registro
        scAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
                return cur.getString(index);
            }
        });

        actContactName.setAdapter(scAdapter);
        actContactName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor)parent.getAdapter().getItem(position);
                edtPhoneNumber.setText(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
        });
    }

    private Cursor buildCursorAutoComplete(CharSequence filter) {
        ContentResolver cr = getContentResolver();
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[]
                {
                        ContactsContract.Data._ID,
                        ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.LABEL
                };
        String selection = String.format("%s='%s'", ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        String[] selectionArgs = null;

        if (filter != null) {
            selection += String.format(" AND %s LIKE ? ", ContactsContract.Data.DISPLAY_NAME);
            selectionArgs = new String[]{"%" + filter + "%"};
        }
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        return cr.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    //endregion

    //region events

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                save();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Const.PICK_CONTACT_PHONE) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        Uri contentUri = data.getData();
        long phoneId = Long.valueOf(contentUri.getLastPathSegment());

        if (phoneId == 0) {
            return;
        }

        Phone phone = (new PhoneBus(this)).get(phoneId);
        Contact contact = (new ContactBus(this)).get(phone.getContactId());

        actContactName.setText(contact.getName());
        edtPhoneNumber.setText(phone.getNumber());
    }

    public void showTimePickerDialog(View v) {
        fAlarmTime.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        fAlarmDate.show(getSupportFragmentManager(), "datePicker");
    }

    public void onContact(View v) {

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, Const.PICK_CONTACT_PHONE);
    }

    public void onDelete(View v) {
        boolean isOk = scheduleBus.delete(scheduleId);
        Intent intent = new Intent(this, ScheduleListActivity.class);
        intent.putExtra(Const.MESSAGE, isOk ? "Agendamento removido!" : "Ops, alguma coisa deu errado! :(");
        startActivity(intent);

        intent = new Intent(this, CallBroadcastReceiver.class);
        intent.putExtra(Const.SCHEDULE_ID, scheduleId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), (int) (scheduleId), intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void onSave(View v) {
        save();
    }

    public void onReturn(View v)
    {
        finish();
    }

    //endregion

}
