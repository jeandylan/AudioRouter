package audiomix.audiooutputsource.scheduler.soundPolicy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by dylan on 16-Aug-17.
 */

public enum SoundPolicySchedulerSettings {
    monday(false,0,0),tuesday(false,0,0),wednesday(false,0,0),thursday(false,0,0),friday(false,0,0),saturday(false,0,0),sunday(false,0,0),alwaysOn(false,-1,-1);
    boolean on;
    int startTime;
    int endTime;
    final static String PREF_NAME="SoundPolicySchedulerSettings";
    final static String TAG="CON_SoundSche_Sett";
    static boolean  isSettingLoaded=false;
    public static boolean isSettingLoaded() {
        return isSettingLoaded;
    }

    public static void setSettingLoaded(boolean settingLoaded) {
        isSettingLoaded = settingLoaded;
    }
    SoundPolicySchedulerSettings(boolean on, int startTime, int endTime) {
        this.on = on;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void update(boolean on, int startTime, int endTime){
        this.on = on;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isOn() {
        checkIsSettingLoaded();
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getStartTime() {
      checkIsSettingLoaded();
        return startTime;
    }

    public void setStartTime(int startTime) {
        if(this==alwaysOn) return;
        this.startTime = startTime;
    }

    public int getEndTime() {
        checkIsSettingLoaded();
        return endTime;
    }
    public String getEndTimeString(){
        return convertMinToHour(getEndTime());
    };
    public String getStartTimeString(){
        return convertMinToHour(getStartTime());
    };

    private  String convertMinToHour(int min){
        long hours = min / 60;
        long remainMinutes = min % 60;
        return String.format("%02d:%02d", hours, remainMinutes);
    }


    public void setEndTime(int endTime) {
        if(this==alwaysOn) return;
        this.endTime = endTime;
    }

    public static SoundPolicySchedulerSettings getTodayScheduler(){
        checkIsSettingLoaded();
        if(alwaysOn.isOn()){
            Log.d(TAG,"always on") ;
            return  alwaysOn;
        }
        Calendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                return sunday;
            case Calendar.MONDAY:
                return  monday;
            case Calendar.TUESDAY:
                return tuesday;
            case Calendar.WEDNESDAY:
                return wednesday;
            case Calendar.THURSDAY:
                return thursday;
            case Calendar.FRIDAY:
                return friday;
            case Calendar.SATURDAY:
                return saturday;
            default:
                throw new  NumberFormatException();

        }
    }

    public static SoundPolicySchedulerSettings getScheduler(int dayId){
       checkIsSettingLoaded();
        switch (dayId) {
            case Calendar.SUNDAY:
                return sunday;
            case Calendar.MONDAY:
                return monday;
            case Calendar.TUESDAY:
                return tuesday;
            case Calendar.WEDNESDAY:
                return wednesday;
            case Calendar.THURSDAY:
                return thursday;
            case Calendar.FRIDAY:
                return friday;
            case  Calendar.SATURDAY:
                return saturday;
            default:
                throw new NumberFormatException("not a day id");
        }
    }

    public static  void saveSetting(Context ctx){
        SharedPreferences settings= ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("ALWAYS_ON",alwaysOn.isOn());
        if(alwaysOn.isOn()){
            editor.apply(); //save it
            return;// no need to save other settings,always On
        }
       SoundPolicySchedulerSettings daySetting;//current audio policy. setting i.e the unsaved instance
        int[] days ={Calendar.SUNDAY,Calendar.MONDAY,Calendar.TUESDAY,Calendar.WEDNESDAY,Calendar.THURSDAY,Calendar.FRIDAY,Calendar.SATURDAY};
        for(int i=0;i<days.length;i++){
            daySetting= getScheduler(days[i]);//get current unsaved enum based on id,iterate trow all day of week
            editor.putBoolean(String.valueOf(i)+"_ON",daySetting.isOn());
            editor.putInt(String.valueOf(i)+"_START",daySetting.getStartTime());
            editor.putInt(String.valueOf(i)+"_END",daySetting.getEndTime());
        }
        editor.apply(); //save it
    }

    public static void loadSetting(Context ctx){
        SharedPreferences settings= ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        setSettingLoaded(true);
        int[] days ={Calendar.SUNDAY,Calendar.MONDAY,Calendar.TUESDAY,Calendar.WEDNESDAY,Calendar.THURSDAY,Calendar.FRIDAY,Calendar.SATURDAY};
        SoundPolicySchedulerSettings daySetting; //default instance as is in memory
        for(int i=0;i<days.length;i++){
            daySetting= getScheduler(days[i]);//get current unsaved enum based on id,iterate trow all day of week
            daySetting.setOn(settings.getBoolean(String.valueOf(i)+"_ON",daySetting.isOn())); //2nd argument , if file not found load current instance value
            daySetting.setStartTime(settings.getInt(String.valueOf(i)+"_START",daySetting.getStartTime())); //2nd argument , if file not found load current instance value
            daySetting.setEndTime(settings.getInt(String.valueOf(i)+"_END",daySetting.getEndTime())); //2nd argument , if file not found load current instance value
        }
        //do load the setting for always on
        alwaysOn.setOn(settings.getBoolean("ALWAYS_ON",false));

    }
    public static boolean  canApply(){ //cannot return nor
        checkIsSettingLoaded();
        SoundPolicySchedulerSettings todaySetting=getTodayScheduler();
        if(todaySetting.getStartTime()==-1)return true; //means always on is set
        Calendar calendar = new GregorianCalendar();
        int currentTime=calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
        Log.d(TAG,"today scheduler for: "+calendar.get(Calendar.DAY_OF_WEEK)+" is on:"+todaySetting.isOn()+" start: "+ todaySetting.getStartTime()+ "end: "+todaySetting.getEndTime());
        return (todaySetting.isOn() && currentTime >= todaySetting.getStartTime() &&  currentTime <= todaySetting.getEndTime());

    }

    @Nullable
    public static Long  getNextUpdateTimeInMillis(){ //return null if no scheduler, -1 if always on
        checkIsSettingLoaded();
        SoundPolicySchedulerSettings todayScheduler=getTodayScheduler();

        if(todayScheduler.getStartTime()==-1) return (long)-1;

        long todaySchedulingStartTimeInMillis=todayScheduler.getStartTime()*60000 +todayMidnightToMillis();
        long todaySchedulingEndTimeInMillis=todayScheduler.getEndTime()*60000 +todayMidnightToMillis();
        Calendar calendar=Calendar.getInstance();
        long currentTodayTimeInMillis=(calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE))*60000+todayMidnightToMillis();

        if(!todayScheduler.isOn()) return null; //means no scheduled today
        if(currentTodayTimeInMillis > todaySchedulingEndTimeInMillis) return null;


        if(currentTodayTimeInMillis==todaySchedulingStartTimeInMillis) return todaySchedulingEndTimeInMillis;
        if(currentTodayTimeInMillis==todaySchedulingEndTimeInMillis) return todaySchedulingStartTimeInMillis;

        return (currentTodayTimeInMillis > todaySchedulingStartTimeInMillis)
                ? todaySchedulingEndTimeInMillis
                :todaySchedulingStartTimeInMillis;


    }

    public static long todayMidnightToMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    public static void checkIsSettingLoaded(){
        if(!isSettingLoaded()) try {
            Log.d(TAG,"setting not loaded");
            throw  new Exception("load settings first");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
