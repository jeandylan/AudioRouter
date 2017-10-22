package audiomix.audiooutputsource.soundPolicy.asyncTask;

import android.os.AsyncTask;

import audiomix.audiooutputsource.soundPolicy.SoundPolicyEnforcer;

/**
 * Created by dylan on 23-Aug-17.
 */

public class AsyncApplySoundPolicyEnforcersPref extends AsyncTask<SoundPolicyEnforcer,Void,Void> {




    @Override
    protected Void doInBackground(SoundPolicyEnforcer... soundPolicyEnforcers) {
        for (int i = 0; i < soundPolicyEnforcers.length; i++) {
            soundPolicyEnforcers[i].applyFromPref();
        }
        return null;
    }
}
