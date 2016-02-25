package com.unit5app;

import com.unit5app.utils.Utils;

/**
 * This class is used to handle periods for the EndOfHourHandler.
 * @author Andrew
 * @version 2/25/16
 */
public class Period {

    private String period, endOfPeriod;

    public Period(int hour, String endOfPeriod) {
        this.endOfPeriod = endOfPeriod;
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

    public String getEndOfPeriod() {
        return endOfPeriod;
    }
}
