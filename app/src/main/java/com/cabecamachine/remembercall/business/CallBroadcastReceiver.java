package com.cabecamachine.remembercall.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.cabecamachine.remembercall.common.Const;

public class CallBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        long scheduleId = intent.getLongExtra(Const.SCHEDULE_ID, 0);
        CallNotification.notify(context, scheduleId);

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
