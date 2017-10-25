package app_info;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/*
* Time converting
*
* Created by kostya on 2/8/2017.
*/
public enum TimeModule {

    @SuppressWarnings("unused")
    INSTANCE;

    // for use string on jsp page
    public static String getDate(java.sql.Timestamp timestamp, String dateFormat) {
        return "" + (Objects.isNull(timestamp) ? "" : new SimpleDateFormat(dateFormat).format(timestamp));
    }

    public static Timestamp getCurrentDate(){
        return new Timestamp(new java.util.Date().getTime());
    }

    @SuppressWarnings("WeakerAccess")
    public static Timestamp startOfDay(Timestamp timestamp){

        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return new Timestamp(c.getTime().getTime());

    }

    public static Timestamp startOfCurrentDay(){
        return startOfDay(getCurrentDate());
    }

    public static java.sql.Date getFinalDateOfProcess(){

        java.sql.Date result = new java.sql.Date( new java.util.Date().getTime() );

        Calendar c = Calendar.getInstance();
        c.setTime(result);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        // +5 work days
        switch (dayOfWeek) {
            case (Calendar.MONDAY): c.add(Calendar.DAY_OF_MONTH, 5);break;
            case (Calendar.SUNDAY): c.add(Calendar.DAY_OF_MONTH, 6);break;
            default: c.add(Calendar.DAY_OF_MONTH, 7);break;
        }

        return new java.sql.Date(c.getTime().getTime());
    }

    public static java.sql.Date getFinalDateOfProcess(int addDays){

        java.sql.Date result = new java.sql.Date( new java.util.Date().getTime() );
        Calendar c = Calendar.getInstance();
        c.setTime(result);
        c.add(Calendar.DAY_OF_MONTH, addDays);
        return new java.sql.Date(c.getTime().getTime());

    }


    public static java.sql.Timestamp addSecondsToCurrentTime(Long seconds){

        return new Timestamp(System.currentTimeMillis() + (seconds * 1000));
    }
}
