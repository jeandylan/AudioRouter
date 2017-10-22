package audiomix.audiooutputsource.hardware.asyncTask;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

import audiomix.audiooutputsource.hardware.device.Device;
import audiomix.audiooutputsource.hardware.device.DeviceManager;

/**
 * Created by dylan on 15-Aug-17.
 */

public class AsyncSetDeviceState extends AsyncTask<Device,Void,Void> {


    public AsyncSetDeviceState() {

    }

    @Override
    protected Void doInBackground(Device ...devices) {
        Method setDeviceConnectionStateMethod;
        Class<?> audioSystem;
        for (int i = 0; i < devices.length; i++) {
            try {
                audioSystem = Class.forName("android.media.AudioSystem");
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){ //is higher than 5.0
                    setDeviceConnectionStateMethod = audioSystem.getMethod("setDeviceConnectionState", int.class, int.class, String.class, String.class);
                }
                else {
                    setDeviceConnectionStateMethod = audioSystem.getMethod("setDeviceConnectionState", int.class, int.class, String.class);
                }


                if (devices[i].getState() == Device.androidDefault) {
                    Log.d("CON_ASYN_DEV", "try to apply default");
                    setDeviceState(audioSystem, setDeviceConnectionStateMethod, devices[i], DeviceManager.DEVICE_OUT_DEFAULT);
                    //may be a fix  //setDeviceConnectionState.invoke(audioSystem, devices[i].getId(), DeviceManager.DEVICE_STATE_UNAVAILABLE, devices[i].getAddress(), devices[i].getName());
                    //  setDeviceConnectionState.invoke(audioSystem, devices[i].getId(), DeviceManager.DEVICE_OUT_DEFAULT, devices[i].getAddress(), devices[i].getName());
                }
                else {
                    setDeviceState(audioSystem, setDeviceConnectionStateMethod, devices[i]);
                }

            } catch (Exception e) {
                Log.e("CON_Asyn_set_State", "setDeviceConnectionState failed: " + e);
                throw new ExceptionInInitializerError("error at set device state");
            }

        }
        return null;
    }



    public void setDeviceState(Class methodClass,Method setDeviceConnectionState,Device device){
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
                setDeviceConnectionState.invoke(methodClass, device.getId(), device.getState(), device.getAddress(), device.getName());
            }
            else{
                setDeviceConnectionState.invoke(methodClass, device.getId(), device.getState(), device.getAddress());
            }
        } catch (Exception ex){
            Log.e("CON_Asyn_set_State", "setDeviceConnectionState failed: " + ex);
            throw new ExceptionInInitializerError("error at set device state");
        }
    }
    public void setDeviceState(Class methodClass,Method setDeviceConnectionState,Device device,int deviceState){
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                setDeviceConnectionState.invoke(methodClass, device.getId(), deviceState, device.getAddress(), device.getName());
            }
            else{
                setDeviceConnectionState.invoke(methodClass, device.getId(),deviceState, device.getAddress());
            }
        } catch (Exception ex){
            Log.e("CON_Asyn_set_State", "setDeviceConnectionState failed: " + ex);
            throw new ExceptionInInitializerError("error at set device state");
        }
    }
}
