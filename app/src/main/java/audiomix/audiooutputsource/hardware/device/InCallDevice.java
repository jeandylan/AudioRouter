package audiomix.audiooutputsource.hardware.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by dylan on 17-Aug-17.
 */

public class InCallDevice extends Device {

    Context ctx;
    int inCallSettingPolicy= -1;
    final String TAG="CON_INCALLDEVICE";
    final String devicePref="deviceIdForCall";

  public InCallDevice(Context ctx){
        super(ctx);
        this.ctx=ctx;
        super.PREF_NAME="InCallDevice";
    }

    public InCallDevice setInCallSettingPolicy(int inCallSettingPolicy) {
        this.inCallSettingPolicy = inCallSettingPolicy;
        return this;
    }

    @Override
    public void applyPref() {
        AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        switch (getPref()) {
            case 0: //default,or no settings
                return;
            case 1: //earpiece
                new Device(DeviceManager.DEVICE_OUT_WIRED_HEADSET,ctx).setDeviceOff();
                new Device(DeviceManager.DEVICE_OUT_WIRED_HEADPHONE,ctx).setDeviceOff();
                audioManager.setSpeakerphoneOn(false);
                return;
            case 2: //loud speaker
                audioManager.setSpeakerphoneOn(true);
                return;
            case 3://headphone
                new Device(DeviceManager.DEVICE_OUT_WIRED_HEADSET,ctx).setDeviceOn();
                new Device(DeviceManager.DEVICE_OUT_WIRED_HEADPHONE,ctx).setDeviceOn();
                return;
        }
    }
    public void applyInCallSettings(){
        this.applyPref();
        }
    public void applyNoCallSettings(){
        AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        new Device(DeviceManager.DEVICE_OUT_WIRED_HEADSET,ctx).applyPref();
        new Device(DeviceManager.DEVICE_OUT_WIRED_HEADPHONE,ctx).applyPref();
        audioManager.setSpeakerphoneOn(false);
    }

    @Override
    public void saveToPref() {
        if (pref ==null || inCallSettingPolicy ==-1 ) throw new NullPointerException("device at Device does not contain context or pref or in call policy not set ");
        Log.d(TAG,"SAVE PREF overload save");
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(devicePref,inCallSettingPolicy);
        editor.commit();

    }

    @Override
    public int getPref() {
        if (pref ==null){
            Log.d(TAG,"get pref null");
            throw new NullPointerException("pref not set in get pref ");
        }
        return  pref.getInt(devicePref,0);
    }
}
