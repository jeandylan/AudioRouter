package audiomix.audiooutputsource.scheduler.audioHardware;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import audiomix.audiooutputsource.hardware.device.Device;
import audiomix.audiooutputsource.util.Notification;

/**
 * Created by dylan on 20-Aug-17.
 */

public  class BackgroundAudioHardwareService extends IntentService {
    String TAG="CON_BACK_Audio_Sett";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BackgroundAudioHardwareService() {
        super("background service");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        applySettings(this);
        BackgroundAudioHardwareAlarm.setNextAlarms(getApplicationContext());
    }



    public void applySettings(Context context){

        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        wakeLock.acquire();
        AudioHardwareSchedulerSettings.loadSetting(context);
        Device earpiece=new Device(Device.DEVICE_OUT_EARPIECE,context);
        Device headphone =new Device(Device.DEVICE_OUT_WIRED_HEADPHONE,context);
        Device headset=new Device(Device.DEVICE_OUT_WIRED_HEADSET,context);
        Device speaker =new Device(Device.DEVICE_OUT_SPEAKER,context);

        boolean canApplyAudioHardware=AudioHardwareSchedulerSettings.canApply();
        if(canApplyAudioHardware){
            earpiece.applyPref();
            headphone.applyPref();
            headset.applyPref();
            speaker.applyPref();
            if(AudioHardwareSchedulerSettings.getTodayScheduler().getStartTime()==-1){
                new Notification("Audio Hardware state","Always on settings active",101,context).show();
                return;
            }else {
                new Notification("Audio Hardware state","Custom settings active",101,context).show();
                return;
            }

        }
        else{
            //reset policy settind ,do not reset policy
            earpiece.setDeviceDefaultNoResetPolicy();
            headphone.setDeviceDefaultNoResetPolicy();
            headset.setDeviceDefaultNoResetPolicy();
            speaker.setDeviceDefaultNoResetPolicy();
            new Notification("Audio Hardware State","default system settings active",101,context).show();
        }


        wakeLock.release();

    }





}
