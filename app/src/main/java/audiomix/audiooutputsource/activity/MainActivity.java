package audiomix.audiooutputsource.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import audiomix.audiooutputsource.R;
import audiomix.audiooutputsource.hardware.device.Device;
import audiomix.audiooutputsource.hardware.device.DeviceManager;
import audiomix.audiooutputsource.hardware.device.InCallDevice;
import audiomix.audiooutputsource.scheduler.audioHardware.AudioHardwareSchedulerSettings;
import audiomix.audiooutputsource.scheduler.soundPolicy.SoundPolicySchedulerSettings;
import audiomix.audiooutputsource.soundPolicy.SoundPolicyEnforcer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "CON_MainActivity";
    private boolean isInteracting=false;
    private final int spinnerDefaultPosHardware =2;
    private final int spinnerDefaultPosSoundPolicy =0;
    private final int spinnerDefaultPosInCall=0;
    private final String ADS_ID="ca-app-pub-5027603773616984/4380439547";
    private AdView adView;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    //Audio hardware States
    String[] audioHardwareStates;
    //view binding
    @BindView(R.id.spnWiredHeadSetStatus) Spinner spnWiredHeadsetStatus;
    @BindView(R.id.spnUsbStatus) Spinner spnUsbStatus;
    @BindView(R.id.spnEarpieceStatus) Spinner spnEarpieceStatus;
    @BindView(R.id.spnSpeakerStatus) Spinner spnSpeakerStatus;
   //coorlayout

    String[] audioPolicy;
    //bind for Audio policy menu
    @BindView(R.id.spnMediaSoundRouting) Spinner spnMediaSoundRouting;
    @BindView(R.id.spnSystemSoundRouting) Spinner spnSystemSoundRouting;
    @BindView(R.id.spnCommunicationSoundRouting) Spinner spnCommunicationSoundRouting;

    //in call settings
    @BindView(R.id.spnInCall) Spinner spnInCall;
    //progress bar
    @BindView((R.id.indeterminateBarAudioHardware))
    ProgressBar progressBarAudioHardware;
    @BindView(R.id.indeterminateBarInCall)
            ProgressBar progressBarInCall;
    //Devices
    Device wiredHeadset;
    Device wiredHeadphone;
    Device speaker;
    Device earpiece;
    Device usb;

    //policy
    SoundPolicyEnforcer soundPolicyEnforcerMedia;
    SoundPolicyEnforcer soundPolicyEnforcerCommunication;
    SoundPolicyEnforcer soundPolicyEnforcerSystem;

    //schedule Job
    @OnClick({R.id.btnSoundPolicyEnforcerSchedulerSettings,R.id.btnAudioHardwareSchedulerSettings})
    public void schedulerBtnClick(Button button){
        Intent intent=new Intent(this, SchedulerActivity.class).setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));
       intent.putExtra("btnClicked",button.getResources().getResourceEntryName(button.getId())); //sent bundle with name of btn click
        startActivity(intent);
    }

    @OnClick(R.id.btnInCallSchedulerSettings)
    public void applyInCall(Button button){
        InCallDevice inCallDevice=new InCallDevice(getApplicationContext()).setInCallSettingPolicy(spnInCall.getSelectedItemPosition());
        inCallDevice.saveToPref();
        schedulerBtnClick(button);
    }
    @OnItemSelected(R.id.spnInCall) //in call Settings
    public void spinnerInCallAudioSelected(Spinner spinner,int position){
        if(!isInteracting)return;
       InCallDevice inCallDevice=new InCallDevice(getApplicationContext()).setInCallSettingPolicy(position);
        inCallDevice.saveToPref();
    }
    @OnClick(R.id.btnHardwareApply)
    public void applyHardwaresSetting(Button button){
        button.setEnabled(false);
        progressBarAudioHardware.setVisibility(View.VISIBLE);
        boolean mustResetPolicyForSpeaker=applyHardwareSetting(spnSpeakerStatus,speaker);
        boolean mustResetPolicyForEarpiece=applyHardwareSetting(spnEarpieceStatus,earpiece);
        boolean mustResetPolicyForWiredHeadset=applyHardwareSetting(spnWiredHeadsetStatus,wiredHeadset);
        boolean mustResetPolicyForWiredHeadphone=applyHardwareSetting(spnWiredHeadsetStatus,wiredHeadphone);
        if(mustResetPolicyForSpeaker||mustResetPolicyForEarpiece|| mustResetPolicyForWiredHeadphone||mustResetPolicyForWiredHeadset){
            speaker.resetPolicy(); // could use any device all does the same
        }
        Snackbar.make(coordinatorLayout, "settings applied", Snackbar.LENGTH_LONG).show();
        progressBarAudioHardware.setVisibility(View.GONE);
        button.setEnabled(true);

    }

    public boolean applyHardwareSetting(Spinner spinner,Device device){
        //
        //*optimized apply setting , prevent reset audio policy for every device , should return true in case Adio policy must be reset
        //*other function can use the output to  reset policy only once
        //
        boolean mustResetAudioPolicy=false;
        if(spinner.getSelectedItemPosition()==spinnerDefaultPosHardware){
            device.setDeviceDefaultNoResetPolicy();
            mustResetAudioPolicy=true;
        }else {
            device.setState(spinner.getSelectedItemPosition());
        }
        device.saveToPref();
        return mustResetAudioPolicy;
    }

    @OnClick(R.id.btnSoundPolicyEnforcerApply)
    public void applySoundEnforcer(Button button){
        button.setEnabled(false);
        progressBarInCall.setVisibility(View.VISIBLE);
        soundEnforcerSpnApply(soundPolicyEnforcerCommunication,spnCommunicationSoundRouting);
        soundEnforcerSpnApply(soundPolicyEnforcerMedia,spnMediaSoundRouting);
        soundEnforcerSpnApply(soundPolicyEnforcerSystem,spnSystemSoundRouting);
       Snackbar.make(coordinatorLayout, "settings applied", Snackbar.LENGTH_LONG).show();
        button.setEnabled(true);
       progressBarInCall.setVisibility(View.GONE);

    }
    public void soundEnforcerSpnApply(SoundPolicyEnforcer soundPolicyEnforcer,Spinner spinner){
        switch (spinner.getSelectedItemPosition()){
            case 0:
                soundPolicyEnforcer.setForceNone();
                break;
            case 1:
                soundPolicyEnforcer.setForceSpeaker();
                break;
            case 2:
                soundPolicyEnforcer.setForceHeadphones();
                break;
            case 3:
                soundPolicyEnforcer.setForceWiredAccessory();
                break;
            case 4:
                soundPolicyEnforcer.setForceSystemEnforced();
                break;
            default:
                return;
        }
        soundPolicyEnforcer.saveToPref();




    }





    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        isInteracting=true;
    }
    @Override
    protected void onStart(){
        super.onStart();
        this.isInteracting=false;
    }
    public void initDevices(){
        wiredHeadset=new Device(DeviceManager.DEVICE_OUT_WIRED_HEADSET,this);
        wiredHeadphone =new Device(DeviceManager.DEVICE_OUT_WIRED_HEADPHONE,this);
        speaker= new Device(DeviceManager.DEVICE_OUT_SPEAKER,this);
        earpiece= new Device(DeviceManager.DEVICE_OUT_EARPIECE,this);
        usb= new Device(DeviceManager.DEVICE_OUT_USB_DEVICE,this);
    }

    public  void initSoundPolicyEnforcers(){
        soundPolicyEnforcerCommunication=new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_COMMUNICATION,this);
        soundPolicyEnforcerMedia= new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_MEDIA,this);
        soundPolicyEnforcerSystem=new SoundPolicyEnforcer(SoundPolicyEnforcer.POLICY_SYSTEM,this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        askPermission();
        ButterKnife.bind(this);
        initDevices(); //init devices
        initSoundPolicyEnforcers();//init policy Enforcer
        initAds();

        audioPolicy=getResources().getStringArray(R.array.audio_sound_policy);
        audioHardwareStates=getResources().getStringArray(R.array.audio_hardware_states);

        dataToSpinner(audioHardwareStates,spnWiredHeadsetStatus, spinnerDefaultPosHardware);
        dataToSpinner(audioHardwareStates,spnUsbStatus, spinnerDefaultPosHardware);
        dataToSpinner(audioHardwareStates,spnEarpieceStatus, spinnerDefaultPosHardware);
        dataToSpinner(audioHardwareStates,spnSpeakerStatus, spinnerDefaultPosHardware);
        //audio policy Spinner
        dataToSpinner(audioPolicy,spnMediaSoundRouting, spinnerDefaultPosSoundPolicy);
        dataToSpinner(audioPolicy,spnCommunicationSoundRouting, spinnerDefaultPosSoundPolicy);
        dataToSpinner(audioPolicy,spnSystemSoundRouting, spinnerDefaultPosSoundPolicy);
            //in call audio binding
        dataToSpinner(getResources().getStringArray(R.array.in_call_hardware),spnInCall, spinnerDefaultPosInCall);
        syncAudioHardwareStateWithUi();
        syncSoundPolicyStateWithUi();
        synInCallDeviceStateWithUI();
    }

    public void initAds(){
       MobileAds.initialize(this, ADS_ID); //init ads
        adView = (AdView)findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .build();
        adView.loadAd(request);

    }
    public void syncAudioHardwareStateWithUi(){
        AudioHardwareSchedulerSettings.loadSetting(getApplicationContext());
         spnWiredHeadsetStatus.setSelection(wiredHeadset.getPref());
         spnEarpieceStatus.setSelection(earpiece.getPref());
         spnSpeakerStatus.setSelection(speaker.getPref());
         spnUsbStatus.setSelection(usb.getPref());


    }

    public void syncSoundPolicyStateWithUi(){
       SoundPolicySchedulerSettings.loadSetting(getApplicationContext());
          soundEnforcerPrefSyncWithSpinner(soundPolicyEnforcerCommunication,spnCommunicationSoundRouting);
            soundEnforcerPrefSyncWithSpinner(soundPolicyEnforcerSystem,spnSystemSoundRouting);
            soundEnforcerPrefSyncWithSpinner(soundPolicyEnforcerMedia,spnMediaSoundRouting);
    }

    public  void synInCallDeviceStateWithUI(){
        InCallDevice inCallDevice =new InCallDevice(getApplicationContext());
        spnInCall.setSelection(inCallDevice.getPref());
    }

    public void soundEnforcerPrefSyncWithSpinner(SoundPolicyEnforcer soundPolicyEnforcer,Spinner spinner) {
        switch (soundPolicyEnforcer.getPref()) {
            case SoundPolicyEnforcer.FORCE_NONE:
                spinner.setSelection(0);
                break;
            case SoundPolicyEnforcer.FORCE_SPEAKER:
                spinner.setSelection(1);
                break;
            case SoundPolicyEnforcer.FORCE_HEADPHONES:
                spinner.setSelection(2);
                break;
            case SoundPolicyEnforcer.FORCE_WIRED_ACCESSORY:
                spinner.setSelection(3);
                break;
            case SoundPolicyEnforcer.FORCE_SYSTEM_ENFORCED:
                spinner.setSelection(4);
                break;
            default:
                return;
        }
    }





    private  void askPermission(){
        if (
                (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_DENIED)
                        ||(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED)
                        ||(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_DENIED)
                        ||(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_DENIED)
                        ||(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_DENIED)
                        ||(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED)
                        ||(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED)
                )
        {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_PHONE_STATE,Manifest.permission.MODIFY_AUDIO_SETTINGS,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.WAKE_LOCK,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        }
                    }).check();
        }
        Log.d("permission","1");
    }



    public void dataToSpinner(String[] data,Spinner spinner,int defaultPos){
         ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(defaultPos);
    }
}






