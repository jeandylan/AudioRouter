package audiomix.audiooutputsource.hardware.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import audiomix.audiooutputsource.hardware.asyncTask.AsyncApplyDevicesPref;
import audiomix.audiooutputsource.hardware.asyncTask.AsyncGetDeviceState;
import audiomix.audiooutputsource.hardware.asyncTask.AsyncSetDeviceState;
import audiomix.audiooutputsource.soundPolicy.SoundPolicyEnforcer;

/**
 * Created by dylan on 05-Aug-17.
 */

public class Device extends DeviceManager {
    final String TAG="CON_DEVICE";
    String PREF_NAME="Device";
    int id;
    int state; //
    SharedPreferences pref;
    Context context;
    String address="";


   public  static final int on=1;
    public static final  int off=0;
    public static final int androidDefault=2; //this does nothing in android  as only 1 or 0 state exits, but special method is need for system default

    public Device(Context ctx){
        this.pref =ctx.getSharedPreferences(PREF_NAME, 0);
    }
    public Device(int id) {
        this.id = id;
    }



    public Device(int id, Context deviceContext) {
        this.id = id;
        this.state=androidDefault;
        this.context=deviceContext;
        this.pref = context.getSharedPreferences(PREF_NAME, 0);
    }

    public String getAddress() {
        return address;
    }

    public void setState(int state) {
        switch (state){
            case on:
                setDeviceOn();
                break;
            case off:
                setDeviceOff();
                break;
            case androidDefault:
                setDeviceSystemDefault();
                break;
            default:
                throw  new ExceptionInInitializerError("state must be within 1 and 2");
        }
        this.state = state;
    }

    public String getName(){
        return super.getDeviceName(id);
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public void setDeviceOff(){
        this.state=off;
        applyState();
    }

    public void setDeviceOn(){
        this.state=on;
        applyState();
    }

    private void applyState(){
        try {
            AsyncSetDeviceState asyncSetDeviceState=new AsyncSetDeviceState();
            asyncSetDeviceState.execute(this);
            asyncSetDeviceState.get();
        }
        catch (Exception e){

        }

    }

    public  void setDeviceSystemDefault(){
        state=androidDefault;
        try {
            applyState();
           resetPolicy();
        }
        catch (Exception e){

        }
    }

    public  void setDeviceDefaultNoResetPolicy(){
        state=androidDefault;
        applyState();
    }

    public void resetPolicy(){
        SoundPolicyEnforcer soundPolicyEnforcerCommunication=new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_COMMUNICATION,context);
        soundPolicyEnforcerCommunication.setForceDefault();

        SoundPolicyEnforcer soundPolicyEnforcerMedia= new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_MEDIA,context);
        soundPolicyEnforcerMedia.setForceDefault();

        SoundPolicyEnforcer soundPolicyEnforcerSystem=new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_SYSTEM,context);
        soundPolicyEnforcerSystem.setForceDefault();
    }

    public void applyPref(){
        if(context==null) throw new ExceptionInInitializerError("context not set for device to run applyFromPref");
        AsyncApplyDevicesPref asyncApplyDevicesPref=new AsyncApplyDevicesPref();
        asyncApplyDevicesPref.execute(this);

    }

    public int getDeviceState(){
        AsyncGetDeviceState asyncGetDeviceState =new AsyncGetDeviceState();
        asyncGetDeviceState.execute(this);
        try {
            return asyncGetDeviceState.get();
        }
        catch (Exception e){
            Log.d(TAG,"exception on get device state");
        }
        return  2;
    }

    public void saveToPref(){
        if (pref ==null) throw new NullPointerException("device at Device does not contain context or pref");
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(String.valueOf(id),state);
        editor.commit();
    }

    public int getPref(){
        return  pref.getInt(String.valueOf(id),androidDefault);
    }

}
