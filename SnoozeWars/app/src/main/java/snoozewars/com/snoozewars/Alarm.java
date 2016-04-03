package snoozewars.com.snoozewars;

/**
 * Created by Eric on 4/2/16.
 */
public class Alarm {
    int hour;
    int min;
    boolean isSet;

    public Alarm() {
        this.hour = -1;
        this.min = -1;
        this.isSet = false;
    }

    public void setAlarm(int h, int m) {
        this.hour = h;
        this.min = m;
        this.isSet = true;
    }

    public void clearAlarm() {
        this.hour = -1;
        this.min = -1;
        this.isSet = false;
    }

}
