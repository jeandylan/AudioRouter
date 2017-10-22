package audiomix.audiooutputsource.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import audiomix.audiooutputsource.R;
import audiomix.audiooutputsource.scheduler.audioHardware.AudioHardwareSchedulerSettings;
import audiomix.audiooutputsource.scheduler.audioHardware.BackgroundAudioHardwareAlarm;
import audiomix.audiooutputsource.scheduler.incall.InCallSchedulerSettings;
import audiomix.audiooutputsource.scheduler.soundPolicy.BackgroundSoundPolicyAlarm;
import audiomix.audiooutputsource.scheduler.soundPolicy.SoundPolicySchedulerSettings;
import audiomix.audiooutputsource.util.TimePickerFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SchedulerActivity extends AppCompatActivity implements TimePickerFragment.OnUserSetTimeListener {
    final String TAG = "CON_scheduActivi";
    final String TAG_START_TIME = "START_TIME";
    final String TAG_END_TIME = "END_TIME";
    boolean validData=true;
    long scheduleNow=0;
    boolean isInit=false;
    Bundle extras;
    // String btnClicked= getIntent().getStringExtra("btnClicked");
    private int startTime;
    private int endTime;

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    //always on
    @BindView(R.id.switchAlwaysOn)
    Switch swtAlwaysOn;
    //time input
    @BindView(R.id.editTxtStartTime)
    EditText editTxtStartTime;
    @BindView(R.id.editTxtEndTime)
    EditText editTxtEndTime;
    @BindView(R.id.txtLayoutEndTIme)
    TextInputLayout txtLayoutEndTime;
    //days of week checkBox
    @BindView(R.id.chkSun)
    CheckBox chkSun;
    @BindView(R.id.chkMon)
    CheckBox chkMon;
    @BindView(R.id.chkTues)
    CheckBox chkTues;
    @BindView(R.id.chkWed)
    CheckBox chkWed;
    @BindView(R.id.chkThurs)
    CheckBox chkThurs;
    @BindView(R.id.chkFri)
    CheckBox chkFri;
    @BindView(R.id.chkSat)
    CheckBox chkSat;
    //cordinator
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

    //register broadcast
    private String formatTimeMinToDisplay(int min) {
        long hours = min / 60;
        long remainMinutes = min % 60;
        return String.format("%02d:%02d", hours, remainMinutes);
    }

    public int getStartTimeInMinutes() {
        String startTime = editTxtStartTime.getText().toString();
        int h = Integer.parseInt(startTime.substring(0, 2));
        int m = Integer.parseInt(startTime.substring(3));
        return h * 60 + m;
    }


    public void setStartTime(int startTimeH, int startTimeM) {
        editTxtStartTime.setText(formatTimeForDisplay(startTimeH, startTimeM));
    }

    public void setStartTime(int timeInMinutes) {
        editTxtStartTime.setText(formatTimeMinToDisplay(timeInMinutes));
    }

    public int getEndTimeInMinutes() {
        String endTime = editTxtEndTime.getText().toString();
        int h = Integer.parseInt(endTime.substring(0, 2));
        int m = Integer.parseInt(endTime.substring(3));
        return h * 60 + m;
    }


    public void setEndTime(int endTimeH, int endTimeM) {
        editTxtEndTime.setText(formatTimeForDisplay(endTimeH, endTimeM));

    }

    public void setEndTime(int timeInMinutes) {
        editTxtEndTime.setText(formatTimeMinToDisplay(timeInMinutes));
        isDataValid();
}

    public boolean isDataValid(){
            boolean isValid;
            if (getEndTimeInMinutes() > getStartTimeInMinutes() || swtAlwaysOn.isChecked()) {
                TextInputLayout til = (TextInputLayout) findViewById(R.id.txtLayoutEndTIme);
                til.setError(null);
                isValid = true;

            } else {
                TextInputLayout til = (TextInputLayout) findViewById(R.id
                        .txtLayoutEndTIme);
                til.setError("Time end should be later than time Start");
                isValid = false;
            }
            return isValid;


    }

    public String formatTimeForDisplay(int h, int m) {
        return String.format("%02d", h) + ":"
                + String.format("%02d", m);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        ButterKnife.bind(this);
        extras = getIntent().getExtras();
        if (extras.getString("btnClicked").equals("btnInCallSchedulerSettings")){
            InCallSchedulerSettings.loadSetting(getApplicationContext());
            syncPrefInCallSchedulerWithUI();
        }

        if (extras.getString("btnClicked").equals("btnAudioHardwareSchedulerSettings")){
            AudioHardwareSchedulerSettings.loadSetting(getApplicationContext());
            syncPrefAudioHardwareSchedulerSettingsWithUI();
        }

        if (extras.getString("btnClicked").equals("btnSoundPolicyEnforcerSchedulerSettings")){
            SoundPolicySchedulerSettings.loadSetting(getApplicationContext());
            syncPrefAudioPolicySchedulerWithUI();
        }

        setInit(true);
    }

@OnCheckedChanged(R.id.switchAlwaysOn)
public  void alwaysOnActive(){
    if(!isInit)return;
   setSchedulingSettingsMenu(!swtAlwaysOn.isChecked());
    isDataValid();

}

    @OnClick(R.id.btnSave)
    public void saveSetting() {
        if(!isDataValid()) return;
        if (extras == null) return;
        if (extras.getString("btnClicked").equals("btnInCallSchedulerSettings")){
            saveInCallSchedulerSetting();
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Scheduler setting for in call saved", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        if (extras.getString("btnClicked").equals("btnAudioHardwareSchedulerSettings")){
            saveAudioHardwareSchedulerSettings();
            BackgroundAudioHardwareAlarm.scheduleAlarms(getApplicationContext(),(long)0);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Scheduler setting for audio hardware saved", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        if (extras.getString("btnClicked").equals("btnSoundPolicyEnforcerSchedulerSettings")){
            saveSoundPolicySchedulerSettings();
            BackgroundSoundPolicyAlarm.scheduleAlarms(getApplicationContext(),(long)0);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Scheduler setting for sound policy saved", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @OnClick(R.id.editTxtEndTime)
    public void inputEndTIme(View view) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(fragmentTransaction, TAG_END_TIME);
    }

    @OnClick(R.id.editTxtStartTime)
    public void inputStartTime(View view) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(fragmentTransaction, TAG_START_TIME);
    }


    @Override
    public void onUserSetTime(String tag, int h, int m) {
        if (tag == TAG_START_TIME) setStartTime(h, m);
        if (tag == TAG_END_TIME) setEndTime(h, m);
    }
    public void setSchedulingSettingsMenu(boolean enable){
        editTxtEndTime.setEnabled(enable);
        editTxtStartTime.setEnabled(enable);
        chkMon.setEnabled(enable);
        chkTues.setEnabled(enable);
        chkWed.setEnabled(enable);
        chkThurs.setEnabled(enable);
        chkFri.setEnabled(enable);
        chkSat.setEnabled(enable);
        chkSun.setEnabled(enable);

    }

    private void saveInCallSchedulerSetting() {
        //in call setting does not have alarm as it uses Broadcast, then decide if setting need application
        //always on should always be updated , and saved
        InCallSchedulerSettings.alwaysOn.update(swtAlwaysOn.isChecked(), -1, -1);
        if(!swtAlwaysOn.isChecked()){
            //days, saved
            InCallSchedulerSettings.sunday.update(chkSun.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
            InCallSchedulerSettings.monday.update(chkMon.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
            InCallSchedulerSettings.tuesday.update(chkTues.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
            InCallSchedulerSettings.wednesday.update(chkWed.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
            InCallSchedulerSettings.thursday.update(chkThurs.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
            InCallSchedulerSettings.friday.update(chkFri.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
            InCallSchedulerSettings.saturday.update(chkSat.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        }
        //save settings in file
        InCallSchedulerSettings.saveSetting(getApplicationContext());
        Log.d(TAG, "start time activity:" + InCallSchedulerSettings.sunday.getStartTimeString() + " end: " + InCallSchedulerSettings.sunday.getEndTimeString());
    }

    public void syncPrefInCallSchedulerWithUI() {
        //always on
        swtAlwaysOn.setChecked(InCallSchedulerSettings.alwaysOn.isOn());
        setSchedulingSettingsMenu(!InCallSchedulerSettings.alwaysOn.isOn());
        //time can use whatever days of week as all time are the same for all days
        setStartTime(InCallSchedulerSettings.sunday.getStartTime());
        setEndTime(InCallSchedulerSettings.sunday.getEndTime());
        //days
        chkSun.setChecked(InCallSchedulerSettings.sunday.isOn());
        chkMon.setChecked(InCallSchedulerSettings.monday.isOn());
        chkTues.setChecked(InCallSchedulerSettings.tuesday.isOn());
        chkWed.setChecked(InCallSchedulerSettings.wednesday.isOn());
        chkThurs.setChecked(InCallSchedulerSettings.thursday.isOn());
        chkFri.setChecked(InCallSchedulerSettings.friday.isOn());
        chkSat.setChecked(InCallSchedulerSettings.saturday.isOn());

    }

    private void saveSoundPolicySchedulerSettings(){
        //always on should always be updated , and saved
        SoundPolicySchedulerSettings.alwaysOn.update(swtAlwaysOn.isChecked(), -1, -1);
        if(swtAlwaysOn.isChecked()){
            SoundPolicySchedulerSettings.saveSetting(getApplicationContext());
            return;
        }
            //save other settings,this improve performance as if alwayOn no need to update other settings
            //days
        SoundPolicySchedulerSettings.sunday.update(chkSun.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        SoundPolicySchedulerSettings.monday.update(chkMon.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        SoundPolicySchedulerSettings.tuesday.update(chkTues.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        SoundPolicySchedulerSettings.wednesday.update(chkWed.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        SoundPolicySchedulerSettings.thursday.update(chkThurs.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        SoundPolicySchedulerSettings.friday.update(chkFri.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        SoundPolicySchedulerSettings.saturday.update(chkSat.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
            //update scheduler alarm
        Log.d(TAG,"next time Policy:"+ SoundPolicySchedulerSettings.getNextUpdateTimeInMillis());
        //save all needed settings
        Log.d(TAG,"sound policy Enforcer saved");
        SoundPolicySchedulerSettings.saveSetting(getApplicationContext());

    }

    public void syncPrefAudioPolicySchedulerWithUI() {
        //always on
        swtAlwaysOn.setChecked(SoundPolicySchedulerSettings.alwaysOn.isOn());
        setSchedulingSettingsMenu(!SoundPolicySchedulerSettings.alwaysOn.isOn());
        //time, as all time the same for all days of the week select whatever
        setStartTime(SoundPolicySchedulerSettings.sunday.getStartTime());
        setEndTime(SoundPolicySchedulerSettings.sunday.getEndTime());

        //days
        chkSun.setChecked(SoundPolicySchedulerSettings.sunday.isOn());
        chkMon.setChecked(SoundPolicySchedulerSettings.monday.isOn());
        chkTues.setChecked(SoundPolicySchedulerSettings.tuesday.isOn());
        chkWed.setChecked(SoundPolicySchedulerSettings.wednesday.isOn());
        chkThurs.setChecked(SoundPolicySchedulerSettings.thursday.isOn());
        chkFri.setChecked(SoundPolicySchedulerSettings.friday.isOn());
        chkSat.setChecked(SoundPolicySchedulerSettings.saturday.isOn());

    }

    private void saveAudioHardwareSchedulerSettings() {
        //always on should always be updated , and saved
        AudioHardwareSchedulerSettings.alwaysOn.update(swtAlwaysOn.isChecked(), -1, -1);
        if(swtAlwaysOn.isChecked()){
            //schedule alarm as fast so as settings can be applied
            AudioHardwareSchedulerSettings.saveSetting(getApplicationContext());
            return;

        }
            //save other settings,this improve performance as if alwayOn no need to update other settings
            //days
        AudioHardwareSchedulerSettings.sunday.update(chkSun.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        AudioHardwareSchedulerSettings.monday.update(chkMon.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        AudioHardwareSchedulerSettings.tuesday.update(chkTues.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        AudioHardwareSchedulerSettings.wednesday.update(chkWed.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        AudioHardwareSchedulerSettings.thursday.update(chkThurs.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        AudioHardwareSchedulerSettings.friday.update(chkFri.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        AudioHardwareSchedulerSettings.saturday.update(chkSat.isChecked(), getStartTimeInMinutes(), getEndTimeInMinutes());
        //save settings in file
        AudioHardwareSchedulerSettings.saveSetting(getApplicationContext());
    }

    public void syncPrefAudioHardwareSchedulerSettingsWithUI() {
        //always on
        swtAlwaysOn.setChecked(AudioHardwareSchedulerSettings.alwaysOn.isOn());
        setSchedulingSettingsMenu(!AudioHardwareSchedulerSettings.alwaysOn.isOn());
        //time
        setStartTime(AudioHardwareSchedulerSettings.sunday.getStartTime());
        setEndTime(AudioHardwareSchedulerSettings.sunday.getEndTime());
        //days
        chkSun.setChecked(AudioHardwareSchedulerSettings.sunday.isOn());
        chkMon.setChecked(AudioHardwareSchedulerSettings.monday.isOn());
        chkTues.setChecked(AudioHardwareSchedulerSettings.tuesday.isOn());
        chkWed.setChecked(AudioHardwareSchedulerSettings.wednesday.isOn());
        chkThurs.setChecked(AudioHardwareSchedulerSettings.thursday.isOn());
        chkFri.setChecked(AudioHardwareSchedulerSettings.friday.isOn());
        chkSat.setChecked(AudioHardwareSchedulerSettings.saturday.isOn());

    }





}
