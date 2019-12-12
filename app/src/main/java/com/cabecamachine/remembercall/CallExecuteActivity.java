package com.cabecamachine.remembercall;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cabecamachine.remembercall.business.ScheduleBus;
import com.cabecamachine.remembercall.common.Const;
import com.cabecamachine.remembercall.entities.Schedule;

import java.text.SimpleDateFormat;

public class CallExecuteActivity extends ActionBarActivity {

    private TextView txtContactName;
    private TextView txtPhoneNumber;
    private TextView txtNote;
    private TextView txtAlarm;
    private long scheduleId;
    private String phoneNumber;
    private ScheduleBus scheduleBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_execute);

        scheduleBus = new ScheduleBus(this);

        components();

        Intent intent = getIntent();
        scheduleId = intent.getLongExtra(Const.SCHEDULE_ID, 0);

        if (scheduleId != 0) {
            Schedule schedule = scheduleBus.get(scheduleId);

            if(schedule == null)
            {
                intent = new Intent(this, ScheduleListActivity.class);
                intent.putExtra(Const.MESSAGE, "Ops, o agendamento não foi encontrado.");
                startActivity(intent);
                return;
            }

            phoneNumber = schedule.getPhoneNumber();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            if (!schedule.getNote().isEmpty()) {
                txtNote.setText(schedule.getNote());
                txtNote.setVisibility(View.VISIBLE);
            } else {
                txtNote.setVisibility(View.GONE);
            }

            if (schedule.getContactName().isEmpty()) {
                txtContactName.setText(schedule.getPhoneNumber());
                txtPhoneNumber.setVisibility(View.GONE);
            } else {
                txtContactName.setText(schedule.getContactName());
                txtPhoneNumber.setText(schedule.getPhoneNumber());
                txtPhoneNumber.setVisibility(View.VISIBLE);
            }

            txtAlarm.setText(sdf.format(schedule.getAlarm()));

        }

    }

    private void components() {
        txtContactName = (TextView) findViewById(R.id.txtContactName);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        txtNote = (TextView) findViewById(R.id.txtNote);
        txtAlarm = (TextView) findViewById(R.id.txtAlarm);
    }

    public void onCall(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
        startActivity(intent);
        finish();
    }

    public void onReturn(View v)
    {
        finish();
    }
}
