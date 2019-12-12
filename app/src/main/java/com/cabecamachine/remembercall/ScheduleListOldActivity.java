package com.cabecamachine.remembercall;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cabecamachine.remembercall.business.ScheduleBus;
import com.cabecamachine.remembercall.entities.Schedule;
import com.cabecamachine.remembercall.adapters.ScheduleAdapter;

import java.util.List;


public class ScheduleListOldActivity extends ActionBarActivity {

    private ScheduleBus bus;
    private ListView lv;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list_old);

        bus = new ScheduleBus(this);
        lv = (ListView) findViewById(R.id.lvItems);

        bindLvSchedules();
    }

    private void bindLvSchedules() {
        List<Schedule> list = bus.listOld();
        adapter = new ScheduleAdapter(this, R.id.lvItems, list);
        lv.setAdapter(adapter);
    }

    public void onReturn(View v)
    {
        finish();
    }
}
