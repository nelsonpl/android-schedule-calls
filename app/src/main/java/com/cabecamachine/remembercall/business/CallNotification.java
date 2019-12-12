package com.cabecamachine.remembercall.business;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.cabecamachine.remembercall.CallExecuteActivity;
import com.cabecamachine.remembercall.R;
import com.cabecamachine.remembercall.business.ScheduleBus;
import com.cabecamachine.remembercall.common.Const;
import com.cabecamachine.remembercall.entities.Schedule;


/**
 * Helper class for showing and canceling call
 * notifications.
 * <p/>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class CallNotification {
    private static final String NOTIFICATION_TAG = "Call";

    public static void notify(final Context context, final long scheduleId) {

        Schedule entity =  (new ScheduleBus(context)).get(scheduleId);
        String contactName = entity.getContactName().isEmpty() ? entity.getPhoneNumber() : entity.getContactName();
        String note = entity.getNote();
        Intent callExecute = new Intent(context, CallExecuteActivity.class);
        callExecute.putExtra(Const.SCHEDULE_ID, scheduleId);

        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);

        final String ticker = res.getString(R.string.call_notification_title, contactName);
        final String title = res.getString(R.string.call_notification_title, contactName);;
        final String text = note;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                        // Set required fields, including the small icon, the
                        // notification title, and text.
                .setSmallIcon(R.drawable.ic_stat_call)
                .setContentTitle(title)
                .setContentText(text)

                        // All fields below this line are optional.

                        // Use a default priority (recognized on devices running Android
                        // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        // Provide a large icon, shown with the notification in the
                        // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                        // Set ticker text (preview) information for this notification.
                .setTicker(ticker)

                        // Show a number. This is useful when stacking notifications of
                        // a single type.
                        //.setNumber(number)

                        // If this notification relates to a past or upcoming event, you
                        // should set the relevant time information using the setWhen
                        // method below. If this call is omitted, the notification's
                        // timestamp will by set to the time at which it was shown.
                        // TODO: Call setWhen if this notification relates to a past or
                        // upcoming event. The sole argument to this method should be
                        // the notification timestamp in milliseconds.
                        //.setWhen(...)

                        // Set the pending intent to be initiated when the user touches
                        // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                (int)scheduleId,
                                callExecute,
                                PendingIntent.FLAG_UPDATE_CURRENT))

                        // Show expanded text content on devices running Android 4.1 or
                        // later.
                .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text)
                                .setBigContentTitle(title)
                        //.setSummaryText("Dummy summary text")
                )

                        // Example additional actions for this notification. These will
                        // only show on devices running Android 4.1 or later, so you
                        // should ensure that the activity in this notification's
                        // content intent provides access to the same actions in
                        // another way.
//                .addAction(
//                        R.drawable.ic_action_time,
//                        res.getString(R.string.action_1_hour),
//                        null)
//                .addAction(
//                        android.R.drawable.ic_menu_close_clear_cancel,
//                        res.getString(R.string.action_not),
//                        null)

                        // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        notify(context, builder.build(), scheduleId);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification, final long scheduleId) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, (int)scheduleId, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context, final long scheduleId) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, (int)scheduleId);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}