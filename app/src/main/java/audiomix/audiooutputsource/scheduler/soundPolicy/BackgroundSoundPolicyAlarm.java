package audiomix.audiooutputsource.scheduler.soundPolicy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dylan on 23-Aug-17.
 */

public class BackgroundSoundPolicyAlarm {
    static String  TAG="CON_BACK_Sound_Alarm";
    public BackgroundSoundPolicyAlarm() {
    }
    public static void scheduleAlarms(Context context, Long startTime){
        Intent intentService=new Intent(context, BackgroundSoundPolicyService.class);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intentService, 0);
//set up next alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Log.d(TAG,"next alarm at:-"+startTime);
        if(startTime==null || startTime==-1) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    0,pendingIntent);
            return;
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                startTime,pendingIntent);
    }

    public static void setNextAlarms(Context context){
        SoundPolicySchedulerSettings.loadSetting(context);
        Long nextSoundPolicyAlarm= SoundPolicySchedulerSettings.getNextUpdateTimeInMillis();
        if(nextSoundPolicyAlarm!=null && nextSoundPolicyAlarm != -1){
            BackgroundSoundPolicyAlarm.scheduleAlarms(context,nextSoundPolicyAlarm);
        }
       // if(nextSoundPolicyAlarm==-1) new Notification("sound policy State","Always On settings active",102,context).show();



    }
}
