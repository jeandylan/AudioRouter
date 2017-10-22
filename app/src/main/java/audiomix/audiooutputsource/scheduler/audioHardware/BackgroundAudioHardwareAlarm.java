package audiomix.audiooutputsource.scheduler.audioHardware;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dylan on 20-Aug-17.
 */

 public class BackgroundAudioHardwareAlarm {

    static String  TAG="CON_BACK_Audio_Hard";
    public BackgroundAudioHardwareAlarm() {
    }

    public static void scheduleAlarms(Context context, Long startTime){
        Intent intentService=new Intent(context, BackgroundAudioHardwareService.class);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intentService, 0);
//set up next alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(startTime==null || startTime==-1) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    0,pendingIntent);
           return;
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                startTime,pendingIntent);
    }

    public static void setNextAlarms(Context context){
        AudioHardwareSchedulerSettings.loadSetting(context);
        Long nextAudioHardwareAlarm= AudioHardwareSchedulerSettings.getNextUpdateTimeInMillis();
        if(nextAudioHardwareAlarm!=null && nextAudioHardwareAlarm != -1){
            BackgroundAudioHardwareAlarm.scheduleAlarms(context,nextAudioHardwareAlarm);
            Log.d(TAG,"next audio alarm:-"+nextAudioHardwareAlarm);
        }

    }

}
