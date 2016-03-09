package com.unit5app;

import com.unit5app.utils.Time;
import com.unit5app.utils.Utils;

/**
 * This class is used to handle periods for the EndOfHourHandler.
 * @author Andrew
 * @version 2/25/16
 */
public class Period {

    private String period, endOfPeriod, lateStart_EndOfPeriod;

    public Period(int hour, String endOfPeriod, String lateStart_EndOfPeriod) {
        this.endOfPeriod = endOfPeriod;
        this.lateStart_EndOfPeriod = lateStart_EndOfPeriod;
        switch(hour) {
            case 1:
                period = 1 + Utils.SUPERSCRIPT_ST;
                break;
            case 2:
                period = 2 + Utils.SUPERSCRIPT_ND;
                break;
            case 3:
                period = 3 + Utils.SUPERSCRIPT_RD;
                break;
            default:
                period = hour + Utils.SUPERSCRIPT_TH;
                break;
        }
    }

    public String getPeriod() {
        return period;
    }

    /**
     * returns the end of the period for the current day. If the current date happens to be a late start, then we will return the late start end of period.
     */
    public String getEndOfPeriod() {
        if(Time.isTodayLateStart()) {
            return lateStart_EndOfPeriod;
        }
        return endOfPeriod;
    }

}
