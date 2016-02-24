package com.unit5app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.unit5app.Article;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main utility Class for the Unit5 App. This is where we keep versatile, modular methods that can
 * be used in any class.
 * @author Andrew and Ben
 * @version 2/21/2016
 */
public abstract class Utils {
    private static final String TAG = "Utils"; /* String name passed to the Logging API */

    public static boolean hadInternetOnLastCheck = false;
    
    /**
     * Gets the index location of 'occurrence' character in a string. Case-sensitive.
     * <ul>Examples:</>
     * <li>findNthIndexOf("Hello, world!", "Hello", "1") returns 0.</>
     * <li>findNthIndexOf("You, you, you.", "you", 2) returns 11.</>
     * @param occurrence - the number of instances of 'needle' to find before returning the index.
     * @param needle - the String to search for within 'str'.
     * @param str - the String to search in for 'needle'.
     * @return int index of the 'occurrence' instance of 'needle'.
     *
     * Credit to John Giotta on www.stackOverflow.com
     * <a href="http://stackoverflow.com/questions/5678152/find-the-nth-occurence-of-a-substring-in-a-string-in-java">Source</a>
     */
    public static int findNthIndexOf (String str, String needle, int occurrence)
            throws IndexOutOfBoundsException {
        /* TODO: Test documentation examples for accuracy. */
        int index = -1;
        Pattern p = Pattern.compile(needle, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        while(m.find()) {
            if ((occurrence--) == 0) { /* Found occurrence... */
                index = m.start();
                break;
            }
        }
        if (index < 0) throw new IndexOutOfBoundsException(); /* If 'needle' is not found it WILL
                                                                 crash. */
        return index;
    }

    /**
     * can be used to sort a List of articles by their publishing dates.
     * <br><ul><b>How To Use:</b><li>Collections.sort(articleListToSort, Utils.articlePubDateSorter);</li></ul></br>
     */
    public static Comparator<Article> articlePubDateSorter = new Comparator<Article>() {
        @Override
        public int compare(Article article0, Article article1) {
            if(Time.getDateAsNumber(article1.getPubDate()) < Time.getDateAsNumber(article0.getPubDate())) return -1; //moves article0 up in the index array
            if(Time.getDateAsNumber(article1.getPubDate()) > Time.getDateAsNumber(article0.getPubDate())) return +1; //moves article0 down in the index array
            return 0; //keeps articles at the same position in the index array.
        }
    };

    /**
     * Returns true if the user is connected to the internet in some way.
     * @param context - context of an Acivity
     * @return
     *
     *      Author: Silvio Delgado from StackOverFlow at <a href="http://stackoverflow.com/questions/28168867/check-internet-status-from-the-main-activity">link</a>
     *
     *      (Andrew) I believe we need this method in here because we may want to use this for other classes in the future e.g. 'The updater class.' Mainly because the user may be traveling or randomly lose internet
     *      access whilst using the app, which could cause a crash when updating the Calendar without internet.
     */
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        hadInternetOnLastCheck = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return hadInternetOnLastCheck;
    }

    /*
    Used mainly for titles, this method will make the first letter in every word in the string a Capital.
     */
    public static String toTitleCase(String title) {
        StringBuilder sb = new StringBuilder();
        boolean spaceFound = true;
        title = title.toLowerCase();
        for(char c : title.toCharArray()) {
            if(c == ' ') {
                spaceFound = true;
            } else if(spaceFound) {
                c = Character.toUpperCase(c);
                spaceFound = false;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}