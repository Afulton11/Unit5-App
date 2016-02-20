package com.unit5app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrew on 2/19/2016.
 * The main Utils Class for our unit5 app. This is where we store methods that are 'utility' methods that can be used in any class.
 */
public class Utils {

    /**
     * returns the index location of a character after finding it n times in the string
     * @param occurrence - the amount of occurences before returning the indexed position in the string.
     * @param needle - the character of which to search for, or pattern, or string of characters
     * @param str - the string to search for the char.
     * @return
     *
     *      Credit to John Giotta on stackOverflow <a href="http://stackoverflow.com/questions/5678152/find-the-nth-occurence-of-a-substring-in-a-string-in-java">Source</a>
     */
    public static int findNthIndexOf (String str, String needle, int occurrence)
            throws IndexOutOfBoundsException {
        int index = -1;
        Pattern p = Pattern.compile(needle, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        while(m.find()) {
            if (occurrence-- == 0) {
                index = m.start();
                break;
            }
        }
        if (index < 0) throw new IndexOutOfBoundsException();
        return index;
    }
}
