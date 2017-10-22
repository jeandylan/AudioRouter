package audiomix.audiooutputsource.soundPolicy.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.Method;

import audiomix.audiooutputsource.soundPolicy.SoundPolicyEnforcer;

/**
 * Created by dylan on 15-Aug-17.
 */

public class AsyncSetSoundPolicyEnforcer extends AsyncTask<SoundPolicyEnforcer,Integer,Void> {
    @Override
    protected Void doInBackground(SoundPolicyEnforcer... soundPolicyEnforcers) {
        for (int i = 0; i < soundPolicyEnforcers.length; i++) {
            try {
                Class<?> audioSystemClass = Class.forName("android.media.AudioSystem");
                Method setDevicePolicy = audioSystemClass.getMethod(
                        "setForceUse", int.class, int.class);
                Log.d("CON_Asyn_Sound_Policy", "policy:-"+ soundPolicyEnforcers[i].getId()+"hardware:-"+soundPolicyEnforcers[i].getRoute());
                setDevicePolicy.invoke(audioSystemClass, soundPolicyEnforcers[i].getId(), soundPolicyEnforcers[i].getRoute());
            } catch (Exception e) {
                Log.d("CON_Asyn_Sound_Policy", "error while setting state");
                return  null;
            }

        }
        return null;
    }
}
