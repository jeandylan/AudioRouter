package audiomix.audiooutputsource.hardware.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.Method;

import audiomix.audiooutputsource.hardware.device.Device;

/**
 * Created by dylan on 23-Aug-17.
 */

public class AsyncGetDeviceState extends AsyncTask<Device,Integer,Integer> {

    @Override
    protected Integer doInBackground(Device... devices) {
        try {
            Class<?> audioSystem = Class.forName("android.media.AudioSystem");
            Method getDeviceConnectionState = audioSystem.getMethod(
                    "getDeviceConnectionState", int.class, String.class);
            return (int)getDeviceConnectionState.invoke(audioSystem, devices[0].getId(),devices[0].getAddress());

        } catch (Exception e) {
            Log.e("CON_ASY_GET_STATE", "setDeviceConnectionState failed: " + e);
            return -999;
        }

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

    }
}
