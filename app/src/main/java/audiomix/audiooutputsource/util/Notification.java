package audiomix.audiooutputsource.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.Random;

import audiomix.audiooutputsource.R;
import audiomix.audiooutputsource.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by dylan on 21-Aug-17.
 */

public class Notification {
    String title;
    String msg;
    int id;
    Context context;


    public Notification(String title, String msg,Context context) {
        this.title =StringUtil.toTitleCase(title);
        this.msg = StringUtil.toCapitalizedFirstLetter(msg);
        this.context=context;
        Random rand = new Random();
        this.id = rand.nextInt(400) + 53;;
    }

    public Notification(String title, String msg, int id,Context context) {
        this.title = StringUtil.toTitleCase(title);
        this.msg = StringUtil.toCapitalizedFirstLetter(msg);
        this.id=id;
        this.context=context;

    }

    public Notification show(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_notification_icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(msg);
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId =id;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        return this;
    }

    public void remove(){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancel(this.id);
    }

}
