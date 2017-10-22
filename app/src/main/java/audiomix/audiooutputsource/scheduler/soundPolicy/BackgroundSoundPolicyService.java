package audiomix.audiooutputsource.scheduler.soundPolicy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import audiomix.audiooutputsource.soundPolicy.SoundPolicyEnforcer;
import audiomix.audiooutputsource.soundPolicy.asyncTask.AsyncSetSoundPolicyEnforcer;
import audiomix.audiooutputsource.util.Notification;

/**
 * Created by dylan on 23-Aug-17.
 */

public class BackgroundSoundPolicyService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BackgroundSoundPolicyService() {
        super("back Audio policy");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        applySettings(this);
        BackgroundSoundPolicyAlarm.setNextAlarms(getApplicationContext());
    }
    public void applySettings(Context context){
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        wakeLock.acquire();
        SoundPolicySchedulerSettings.loadSetting(context);

        SoundPolicyEnforcer soundPolicyEnforcerMedia= new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_MEDIA,context).loadPref();
        SoundPolicyEnforcer soundPolicyEnforcerCommunication= new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_COMMUNICATION,context).loadPref();
        SoundPolicyEnforcer soundPolicyEnforcerSystem = new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_SYSTEM,context).loadPref();

        boolean canApplyAudioPolicy= SoundPolicySchedulerSettings.canApply();
        Log.d("CON_ALARM_SO_PO_SERVICE"," can apply"+canApplyAudioPolicy);
        if(canApplyAudioPolicy){
            AsyncSetSoundPolicyEnforcer asyncSetSoundPolicyEnforcer =new AsyncSetSoundPolicyEnforcer();
            asyncSetSoundPolicyEnforcer.execute(soundPolicyEnforcerCommunication,soundPolicyEnforcerMedia,soundPolicyEnforcerSystem);
            if(SoundPolicySchedulerSettings.getTodayScheduler().getStartTime()==-1){
                new Notification("Sound Policy State","Always on settings active",201,context).show();
                Log.d("CON_ALarm_AudioPo","notification should be shown");
                return;
            }else {
                new Notification("Sound Policy State","Custom settings active",201,context).show();
                return;
            }
        }
        //reset to default
        else{
            soundPolicyEnforcerMedia.setForceDefault();
            soundPolicyEnforcerCommunication.setForceDefault();
            soundPolicyEnforcerSystem.setForceDefault();
           new Notification("Sound policy State","default system settings active",201,context).show();
        }

        wakeLock.release();
    }
}
