package com.unit5app;

import android.util.Log;

/**
 * Created by Andrew on 2/19/2016.
 * The main Utils Class for our unit5 app. This is where we store methods that are 'utility' methods that can be used in any class.
 */
public class Utils {

    /**
     * returns the index location of a character after finding it n times in the string
     * @param ordinal - the amount of occurences before returning the indexed position in the string.
     * @param c - the character of which to search for
     * @param string - the string to search for the char.
     * @return
     */
    public static int getOrdinalIndexOfChar(int ordinal, char c, String string) {
       int pos = string.indexOf(c);
        for(int i = ordinal; i > 0 || pos != -1; ordinal--)
            pos = string.indexOf(c, pos + 1);
        Log.d("CalendarEvent", "Pos of space: " + pos);
        return pos;
    }
}
