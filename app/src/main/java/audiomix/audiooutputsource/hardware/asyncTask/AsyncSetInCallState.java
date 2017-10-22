package audiomix.audiooutputsource.hardware.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import audiomix.audiooutputsource.hardware.device.InCallDevice;

/**
 * Created by dylan on 17-Aug-17.
 */

public class AsyncSetInCallState extends AsyncTask<Integer,Integer,Boolean> {
    private Context context;
    private InCallDevice inCallDevice;
    final int onState=1;
    final int offState=2;
    final int defaultState=0;

    public AsyncSetInCallState(Context context, int deviceId) {
        this.context = context;
      //  inCallDevice=new InCallDevice(deviceId,this.context);
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        switch (integers[0]){ //position equals the selection box selection ,should be sync with resource audioHardwareState.xml
            case defaultState:
               // inCallDevice.setDeviceSystemDefault();
                return true;
            case onState:
                //inCallDevice.setDeviceOn();
                return true;
            case offState:
                //inCallDevice.setDeviceOff();
                return true;
            default:
                return false;
        }
    }
}
