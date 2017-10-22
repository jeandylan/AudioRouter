package audiomix.audiooutputsource.scheduler.incall;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by dylan on 15-Aug-17.
 */
public enum InCallSchedulerSettings {
    monday(false,0,0),tuesday(false,0,0),wednesday(false,0,0),thursday(false,0,0),friday(false,0,0),saturday(false,0,0),sunday(false,0,0),alwaysOn(false,-1,-1);
    boolean on;
    int startTime;
    int endTime;
    final static  String TAGInCall="CON_InCallSSchedu";
    final static String PREF_NAME="InCallSchedulerSettings";

    InCallSchedulerSettings(boolean on, int startTime, int endTime) {
        this.on = on;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void update(boolean on, int startTime, int endTime){
        this.on = on;
        this.startTime = startTime;
        this.endTime = endTime;
        Log.d(TAGInCall,"update settings start time:"+startTime);
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
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
        this.endTime = endTime;
    }

    public static  InCallSchedulerSettings getTodayScheduler(){
        if(alwaysOn.isOn()) return  alwaysOn;
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

    public long getNextUpdateTimeInMillis(){
        InCallSchedulerSettings todayScheduler=getTodayScheduler();
        if(todayScheduler.getStartTime()==-1) return -1;
        if(!todayScheduler.isOn()) return 0;
        long todaySchedulingStartTimeInMillis=todayScheduler.getStartTime()*6000;
        long todaySchedulingEndTimeInMillis=todayScheduler.getEndTime()*60000;
        long currentTimeMilli=System.currentTimeMillis();
       return (currentTimeMilli > todaySchedulingStartTimeInMillis) ? todaySchedulingEndTimeInMillis: todaySchedulingStartTimeInMillis;
    }

    public static InCallSchedulerSettings getScheduler(int dayId){
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
    public static boolean  canApply(){ //cannot return nor
        InCallSchedulerSettings todaySetting=getTodayScheduler();
        if(todaySetting.getStartTime()==-1) {
            return true; //means always on is set
        }
        Calendar calendar = new GregorianCalendar();
        int currentTime=calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
        Log.d(TAGInCall,"current minute :"+String.valueOf(currentTime));
        Log.d(TAGInCall,"today: "+calendar.get(Calendar.DAY_OF_WEEK)+" is on:"+todaySetting.isOn()+" start: "+ todaySetting.getStartTime()+ "end: "+todaySetting.getEndTime());
        return (todaySetting.isOn() && currentTime >= todaySetting.getStartTime() &&  currentTime <= todaySetting.getEndTime());


    }

    public static  void saveSetting(Context ctx){
        SharedPreferences settings= ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("ALWAYS_ON",InCallSchedulerSettings.alwaysOn.isOn());
        if(alwaysOn.isOn()){
            editor.commit(); //save it
            return;// no need to save other settings,always On
        }
        InCallSchedulerSettings daySetting;//current incall setting i.e the unsaved instance
        int[] days ={Calendar.SUNDAY,Calendar.MONDAY,Calendar.TUESDAY,Calendar.WEDNESDAY,Calendar.THURSDAY,Calendar.FRIDAY,Calendar.SATURDAY};
        for(int i=0;i<days.length;i++){
            daySetting= getScheduler(days[i]);//get current unsaved enum based on id,iterate trow all day of week
            editor.putBoolean(String.valueOf(i)+"_ON",daySetting.isOn());
            editor.putInt(String.valueOf(i)+"_START",daySetting.getStartTime());
            editor.putInt(String.valueOf(i)+"_END",daySetting.getEndTime());
        }
        editor.commit(); //save it
    }

    public static void loadSetting(Context ctx){
        SharedPreferences settings= ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int[] days ={Calendar.SUNDAY,Calendar.MONDAY,Calendar.TUESDAY,Calendar.WEDNESDAY,Calendar.THURSDAY,Calendar.FRIDAY,Calendar.SATURDAY};
        InCallSchedulerSettings daySetting; //default instance as is in memory
        for(int i=0;i<days.length;i++){
            daySetting= getScheduler(days[i]);//get current unsaved enum based on id,iterate trow all day of week
            boolean on=settings.getBoolean(String.valueOf(i)+"_ON",daySetting.isOn());
            int startTime=settings.getInt(String.valueOf(i)+"_START",daySetting.getStartTime());
            int endTime=settings.getInt(String.valueOf(i)+"_END",daySetting.getEndTime());
            daySetting.update(on,startTime,endTime);
        }
        //do load the setting for always on
        alwaysOn.setOn(settings.getBoolean("ALWAYS_ON",alwaysOn.isOn()));


    }
}
