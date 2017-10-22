package audiomix.audiooutputsource.soundPolicy;

import android.content.Context;
import android.content.SharedPreferences;

import audiomix.audiooutputsource.soundPolicy.asyncTask.AsyncSetSoundPolicyEnforcer;

/**
 * Created by dylan on 06-Aug-17.
 */

public class SoundPolicyEnforcer {

final String TAG="CON_SOUND_POL";
    //states
    public static  int POLICY_COMMUNICATION=0;
    public  static int POLICY_MEDIA=1;
    public static final int POLICY_RECORD = 2;
    public static final int POLICY_DOCK = 3;
    public static final int POLICY_HDMI_SYSTEM_AUDIO = 5;
    public static final int POLICY_ENCODED_SURROUND = 6;
    private static final int POLICY_FORCE_USE = 7;
    public  static  int POLICY_SYSTEM=4;

    public static final int FORCE_NONE = 0;
    public static final int FORCE_SPEAKER = 1;
    public static final int FORCE_HEADPHONES = 2;
    public static final  int FORCE_BT_SCO = 3;
    public static final int FORCE_BT_A2DP = 4;
    public static  final int FORCE_WIRED_ACCESSORY = 5;
    public static final  int FORCE_BT_CAR_DOCK = 6;
    public static final int FORCE_BT_DESK_DOCK = 7;
    public static final int FORCE_ANALOG_DOCK = 8;
    public static final int FORCE_DIGITAL_DOCK = 9;
    public static final int FORCE_NO_BT_A2DP = 10;
    public static final int FORCE_SYSTEM_ENFORCED = 11;
    public static final int FORCE_HDMI_SYSTEM_AUDIO_ENFORCED = 12;
    public static final int FORCE_ENCODED_SURROUND_NEVER = 13;
    public static final int FORCE_ENCODED_SURROUND_ALWAYS = 14;
    public static final int NUM_FORCE_CONFIG = 15;
    public static final int FORCE_DEFAULT = FORCE_NONE;

    //class memeber
    public String name;
    private  int id;
    private int route;
    private int androidDefault=FORCE_NONE;
    private int currentState;
    private final String  PREF_NAME="SOUND_POLICY_ENFORCER";
    SharedPreferences pref;

    public SoundPolicyEnforcer(int id, Context ctx)  {
        this.id = id;
        name ="";
        route=FORCE_NONE;
        this.pref = ctx.getSharedPreferences(PREF_NAME, 0);

    }


    public int getId() {
        return id;
    }

    public int getRoute() {
        return route;
    }

    private void applyState(){
        try {
            AsyncSetSoundPolicyEnforcer asyncSetSoundPolicyEnforcer= new AsyncSetSoundPolicyEnforcer();
            asyncSetSoundPolicyEnforcer.execute(this);
            asyncSetSoundPolicyEnforcer.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setForceNone() {
       route=FORCE_NONE;
        applyState();
    }

    public  void setForceSpeaker() {
        route=FORCE_SPEAKER;
        applyState();

    }

    public  void setForceHeadphones()  {
        route=FORCE_HEADPHONES;
        applyState();
    }

    public  void setForceBtSco()  {
        route=FORCE_BT_SCO;
        applyState();
    }

    public  void setForceBtA2dp() {
        route=FORCE_BT_A2DP;
        applyState();
    }

    public void setForceWiredAccessory()  {
        route=FORCE_WIRED_ACCESSORY;
        applyState();
    }

    public  void setForceBtCarDock()  {
        route=FORCE_BT_CAR_DOCK;
        applyState();
    }

    public void setForceBtDeskDock() {
        route =FORCE_BT_DESK_DOCK;
        applyState();

    }

    public void setForceAnalogDock()  {
       route=FORCE_ANALOG_DOCK;
        applyState();

    }

    public void setForceDigitalDock() {
       route=FORCE_DIGITAL_DOCK;
        applyState();
    }

    public void setForceNoBtA2dp() {
       route=FORCE_NO_BT_A2DP;
        applyState();
    }

    public  void setForceSystemEnforced(){
       route=FORCE_SYSTEM_ENFORCED;
        applyState();

    }

    public void setForceHdmiSystemAudioEnforced() {
       route=FORCE_HDMI_SYSTEM_AUDIO_ENFORCED;
        applyState();
    }

    public  void setForceEncodedSurroundNever()  {
        route=FORCE_ENCODED_SURROUND_NEVER;
        applyState();

    }

    public  void setForceEncodedSurroundAlways()  {
       route=FORCE_ENCODED_SURROUND_ALWAYS;
        applyState();
    }

    public  void setNumForceConfig() {
       route=NUM_FORCE_CONFIG;
        applyState();
    }

    public  void setForceDefault()  {
        route=FORCE_DEFAULT;
        applyState();
    }

    //save settings
    public void saveToPref(){ //pref are save on config change
        if (pref==null) throw new NullPointerException("device at Device does not contain context or settings");
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(String.valueOf(id),route); //change to android state by using reflection in case of error
        editor.apply();
    }

    public int getPref(){
        if (pref==null) throw new NullPointerException("device at Device does not contain context or settings");
        return  pref.getInt(String.valueOf(id),androidDefault);
    }

    public void applyFromPref(){ //apply pre saved pref to device route
        if (pref==null) throw new NullPointerException("device at Device does not contain context or settings");
       route=getPref();
        applyState();
    }
    public SoundPolicyEnforcer loadPref()
    {
        if (pref==null) throw new NullPointerException("device at Device does not contain context or settings");
        route=getPref();
        return this;
    }


}
