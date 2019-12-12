package com.cabecamachine.remembercall;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cabecamachine.remembercall.business.ScheduleBus;
import com.cabecamachine.remembercall.common.Const;
import com.cabecamachine.remembercall.entities.Schedule;
import com.cabecamachine.remembercall.adapters.ScheduleAdapter;

import java.util.List;

public class ScheduleListActivity extends ActionBarActivity {

    private ScheduleBus bus;
    private ListView lv;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        //CallNotification.notify(this, 3);

        bus = new ScheduleBus(this);
        lv = (ListView) findViewById(R.id.lvItems);

        bindLvSchedules();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                long contactId = adapter.getItem(position).getContactId();
                long phoneId = adapter.getItem(position).getPhoneId();
                long scheduleId = adapter.getItem(position).getId();

//                String nameSelection = adapter.getItem(position).getContactName();
//                String idString = String.valueOf(phoneId);
//                Toast.makeText(ScheduleListActivity.this,idString + " - " + nameSelection,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ScheduleListActivity.this, ScheduleActivity.class);
                intent.putExtra(Const.PHONE_ID, phoneId);
                intent.putExtra(Const.CONTACT_ID, contactId);
                intent.putExtra(Const.SCHEDULE_ID, scheduleId);
                startActivity(intent);

            }
        });

        String msg = getIntent().getStringExtra(Const.MESSAGE);
        if(msg != null)
        {
            Toast.makeText(ScheduleListActivity.this, msg, Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        toolbar.setTitle("Ligo Depois");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        Intent intent;

        switch (id)
        {

            case R.id.action_old:
                intent = new Intent(ScheduleListActivity.this, ScheduleListOldActivity.class);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onNew (View v)
    {
        Intent intent = new Intent(ScheduleListActivity.this, ScheduleActivity.class);
        startActivity(intent);
    }

    private void bindLvSchedules() {
        List<Schedule> list = bus.list();

        if(list.size() == 0)
        {
            findViewById(R.id.txtMsg).setVisibility(View.VISIBLE);
            findViewById(R.id.lvItems).setVisibility(View.GONE);
        }
        else {
            adapter = new ScheduleAdapter(this, R.id.lvItems, list);
            lv.setAdapter(adapter);
        }
    }
}
