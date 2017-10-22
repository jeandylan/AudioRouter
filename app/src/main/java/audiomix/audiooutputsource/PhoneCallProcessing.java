package audiomix.audiooutputsource;

import android.app.NotificationManager;
import android.content.Context;

import java.util.Date;

import audiomix.audiooutputsource.hardware.device.InCallDevice;
import audiomix.audiooutputsource.scheduler.incall.InCallSchedulerSettings;
import audiomix.audiooutputsource.util.Notification;
import audiomix.audiooutputsource.util.PhoneCall;

/**
 * Created by dylan on 10-Aug-17.
 */

public class PhoneCallProcessing extends PhoneCall {
    final String TAG="phone call Broadcater";
    InCallDevice inCallDevice;
    int notificationId=102;
    public String IN_CALL_SETTINGS="inCallSettings";

    @Override
    protected void initialized(Context ctx) {
     inCallDevice =new InCallDevice(ctx);


    }

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        InCallSchedulerSettings.loadSetting(ctx);
        if(InCallSchedulerSettings.canApply()){
            new Notification("In call state","custom settings applied",notificationId,ctx).show();
            inCallDevice.applyInCallSettings();
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        inCallDevice.applyNoCallSettings();
       new Notification("In call state","call ended ,off-call settings applied",102,ctx).show();
       NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
       notificationManager.cancel(notificationId);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        InCallSchedulerSettings.loadSetting(ctx);
        if(InCallSchedulerSettings.canApply()){
            inCallDevice.applyInCallSettings();
            new Notification("in call state","custom settings applied",102,ctx).show();
        }
        else {
            new Notification("In call state","no settings applied",102,ctx).show();
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        inCallDevice.applyNoCallSettings();
        new Notification("In call state","call ended ,off-call settings applied",102,ctx).show();
        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
       notificationManager.cancel(notificationId);

    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

    }



}
