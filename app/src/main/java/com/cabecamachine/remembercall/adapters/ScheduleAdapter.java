package com.cabecamachine.remembercall.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cabecamachine.remembercall.R;
import com.cabecamachine.remembercall.entities.Schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {

    private Context context;

    public ScheduleAdapter(Context context, int resourceId,
                           List<Schedule> listItems) {
        super(context, resourceId, listItems);
        this.context = context;
    }

    private class ViewSelected {
        private TextView contactName;
        private TextView phoneNumber;
        private TextView alarm;
        private TextView note;

        public TextView getContactName() {
            return contactName;
        }

        public void setContactName(TextView contactName) {
            this.contactName = contactName;
        }

        public TextView getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(TextView phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public TextView getAlarm() {
            return alarm;
        }

        public void setAlarm(TextView alarm) {
            this.alarm = alarm;
        }

        public TextView getNote() {
            return note;
        }

        public void setNote(TextView note) {
            this.note = note;
        }

    }

    public View getView(int index, View viewConverter, ViewGroup group) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Schedule item = getItem(index);

        ViewSelected select;
        if (viewConverter == null) {
            viewConverter = layoutInflater.inflate(R.layout.us_schedule_item, null);

            select = new ViewSelected();
            select.setContactName((TextView) viewConverter.findViewById(R.id.txtName));
            select.setPhoneNumber((TextView) viewConverter.findViewById(R.id.txtPhone));
            select.setAlarm((TextView) viewConverter.findViewById(R.id.txtAlarm));
            select.setNote((TextView) viewConverter.findViewById(R.id.txtNote));

            viewConverter.setTag(select);
        } else {
            select = (ViewSelected) viewConverter.getTag();
        }

        if (item.getContactName().isEmpty()) {
            select.getContactName().setText(item.getPhoneNumber());
            select.getPhoneNumber().setVisibility(View.GONE);
        } else {
            select.getContactName().setText(item.getContactName());
            select.getPhoneNumber().setText(item.getPhoneNumber());
        }

        //if (item.getNote().isEmpty()) {
        //    select.getNote().setVisibility(View.GONE);
       // } else {
           select.getNote().setText(item.getNote());
        //}

        Calendar today = Calendar.getInstance();
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(item.getAlarm());

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        alarm.set(Calendar.HOUR_OF_DAY, 0);
        alarm.set(Calendar.MINUTE, 0);
        alarm.set(Calendar.SECOND, 0);
        alarm.set(Calendar.MILLISECOND, 0);

        String label = "";
        SimpleDateFormat sdf = new SimpleDateFormat();

        today.add(Calendar.DAY_OF_MONTH, -1);
        if (alarm.getTimeInMillis() == today.getTimeInMillis()) {
            label = "Ontem as ";
            sdf = new SimpleDateFormat("HH:mm");
        } else {
            today.add(Calendar.DAY_OF_MONTH, 1);
            if (alarm.getTimeInMillis() == today.getTimeInMillis()) {
                label = "Hoje as ";
                sdf = new SimpleDateFormat("HH:mm");
            } else {
                today.add(Calendar.DAY_OF_MONTH, 1);
                if (alarm.getTimeInMillis() == today.getTimeInMillis()) {
                    label = "Amanhã as ";
                    sdf = new SimpleDateFormat("HH:mm");
                }
            }
        }

        select.getAlarm().setText(label + sdf.format(item.getAlarm()));

//		if (item.getPhoto() != null) {
//		    Bitmap bResized = Bitmap.createScaledBitmap(item.getPhoto(), 50, 50, false);
//		    Drawable dResized = new BitmapDrawable(context.getResources(), bResized);
//			select.getName().setCompoundDrawablesWithIntrinsicBounds(dResized, null, null, null);
//			} else {
//			//TODO: Existe um bug se retirar esta linha, algumas fotos são repetidas.
//			Drawable img = getContext().getResources().getDrawable( R.drawable.ic_contact_default );
//			img.setBounds(0, 0, 50, 50);
//			select.getName().setCompoundDrawables( img, null, null, null );
//		}

        return viewConverter;
    }
}
