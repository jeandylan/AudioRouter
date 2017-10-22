package audiomix.audiooutputsource.soundPolicy.asyncTask;

import android.os.AsyncTask;

import java.lang.reflect.Method;

import audiomix.audiooutputsource.soundPolicy.SoundPolicyEnforcer;

/**
 * Created by dylan on 24-Aug-17.
 */

public class AsyncGetSoundPolicyEnforcer extends AsyncTask<SoundPolicyEnforcer,Integer,Integer> {
    @Override
    protected Integer doInBackground(SoundPolicyEnforcer... soundPolicyEnforcers) {
        try {
            Class<?> audioSystemClass = Class.forName("android.media.AudioSystem");
            Method getDevicePolicy = audioSystemClass.getMethod(
                    "getForceUse", int.class);

            return (int) getDevicePolicy.invoke(audioSystemClass, soundPolicyEnforcers[0].getId());
        } catch (Exception e) {
            throw  new UnknownError(" something went wrong at sound policy get config");
        }
    }
}
