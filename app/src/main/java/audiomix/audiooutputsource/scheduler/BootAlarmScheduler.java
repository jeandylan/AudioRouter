package audiomix.audiooutputsource.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import audiomix.audiooutputsource.scheduler.audioHardware.AudioHardwareSchedulerSettings;
import audiomix.audiooutputsource.scheduler.audioHardware.BackgroundAudioHardwareAlarm;
import audiomix.audiooutputsource.scheduler.audioHardware.BackgroundAudioHardwareService;
import audiomix.audiooutputsource.scheduler.soundPolicy.BackgroundSoundPolicyAlarm;
import audiomix.audiooutputsource.scheduler.soundPolicy.BackgroundSoundPolicyService;
import audiomix.audiooutputsource.scheduler.soundPolicy.SoundPolicySchedulerSettings;

/**
 * Created by dylan on 20-Aug-17.
 */

public class BootAlarmScheduler extends BroadcastReceiver {
    AlarmManager alarmMgr;
    PendingIntent pendingIntent;

    AlarmManager repeatAlarmMgr;
    PendingIntent repeatingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {

        BackgroundAudioHardwareService backgroundAudioHardwareService =new BackgroundAudioHardwareService();
        AudioHardwareSchedulerSettings.loadSetting(context);
        backgroundAudioHardwareService.applySettings(context);
        //set next alarm for hardware
        BackgroundAudioHardwareAlarm.setNextAlarms(context);
        //repeat alarm
        repeatingServiceAlarmManager(context,BackgroundAudioHardwareService.class);
        BackgroundSoundPolicyService backgroundSoundPolicyService=new BackgroundSoundPolicyService();
        SoundPolicySchedulerSettings.loadSetting(context);
        backgroundSoundPolicyService.applySettings(context);
        //schedule next alarm
        BackgroundSoundPolicyAlarm.setNextAlarms(context);
        //repeat alarm
        repeatingServiceAlarmManager(context,BackgroundSoundPolicyService.class);




    }



    public void repeatingServiceAlarmManager(Context context,Class<?> serviceClass)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        Intent dialogIntent = new Intent(context, serviceClass);
        //Log.d("CON_REPAET TIMER","creat4e new timer");
        repeatAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        repeatingIntent = PendingIntent.getService(context, 0, dialogIntent,PendingIntent.FLAG_CANCEL_CURRENT );
        repeatAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, repeatingIntent);

    }
}
