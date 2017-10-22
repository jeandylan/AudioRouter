package audiomix.audiooutputsource.hardware.asyncTask;

import android.os.AsyncTask;

import audiomix.audiooutputsource.hardware.device.Device;

/**
 * Created by dylan on 18-Aug-17.
 */

public class AsyncApplyDevicesPref extends AsyncTask<Device,Integer,Void> {

    final int onState=1;
    final int offState=2;
    final int defaultState=0;
    public AsyncApplyDevicesPref() {
    }



    @Override
    protected Void doInBackground(Device... devices) {
        for (int i = 0; i < devices.length; i++) {
            switch (devices[i].getPref()){ //position equals the selection box selection ,should be sync with resource audioHardwareState.xml
                case defaultState:
                    devices[i].setDeviceSystemDefault();
                    break;
                case onState:
                    devices[i].setDeviceOn();
                    break;
                case offState:
                    devices[i].setDeviceOff();
                    break;
                default:
                    break;
            }
        }
        return null;
    }



}
