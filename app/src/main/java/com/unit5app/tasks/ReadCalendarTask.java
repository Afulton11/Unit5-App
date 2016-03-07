package com.unit5app.tasks;

import android.os.AsyncTask;
import android.text.Html;

import com.unit5app.activities.UpcomingEventsActivity;
import com.unit5app.calendars.CalendarEvent;
import com.unit5app.com.unit5app.parsers.CalendarRssReader;
import com.unit5app.notifications.MyNotificationHandler;
import com.unit5app.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * reads the feed from the rssReader titled 'rssReader' and sets the article titles seperate items in list view
 **/
public class ReadCalendarTask extends AsyncTask<Void, Void, Void> {

    private CalendarRssReader reader;

    boolean loaded;

    private List<String> methodRequests;

    public ReadCalendarTask(String CalendarRssUrl) {
        this.reader = new CalendarRssReader(CalendarRssUrl);
        this.methodRequests = new ArrayList<>();
        loaded = false;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        String html_styling_string = "";
        for(int i = 0; i < reader.getCalendarEvents().size(); i++) { //goes through every calendar event found in the xml from the calendar and adds a little bit of styling to the text before displaying it on the textview.
            CalendarEvent event = reader.getCalendarEvents().get(i);
            html_styling_string += ("<br><b>&#8226;" + event.getDate() +
                    "</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;Title: " + event.getTitle() +
                    "</br><br>&nbsp;&nbsp;&nbsp;&nbsp;Time Occuring: "+ event.getTimeOccurring() +
                    "</br><br>&nbsp;&nbsp;&nbsp;&nbsp;Type (debug): " + event.getType() + "</br><br></br>");
        }
        UpcomingEventsActivity.calendarEventsString = Html.fromHtml(html_styling_string);
        loaded = true;

//        for(String s : methodRequests) { //http://www.mkyong.com/java/how-to-use-reflection-to-call-java-method-at-runtime/
//            String[] split = s.split("\t");
//            String cls_name = split[0];
//            try {
//                Class<?> cls = Class.forName(cls_name);
//                Object obj = cls.newInstance();
//
//                Method method = cls.getMethod(split[1], new Class[] {}); //only works for methods with no params
//                method.invoke(obj);
//            } catch (Exception e) {
//                Log.d("CALENDAR_TASK", e.getMessage(), e);
//            }
//        }
//        methodRequests.clear();
        MyNotificationHandler.createNotificationsFromSettings();
    }

    @Override
    protected Void doInBackground(Void... params) {
        reader.loadXml();
        Utils.unlockWaiter();
        return null;
    }

    /**
     * adds a method request for when the calendar finished executing <b>NOTE: NOT YET WORKING!!</b>
     * @param methodRequests - the class and method to call. (only works for methods with no params as of right now.)
     *                       <br>format of a method Request string: "package\tmethodName"</br>
     *                       <br>So calling MyNotificationHandler.createNotificationsFromSettings(), Would work as follows: </br>
     *                       <br>addMethodRequests(new String[] {"com.unit5app.notifications.MyNotificationHandler\tcreateNotificationsFromSetting"})</br>
     */
    public void addMethodRequests(final String...methodRequests) {
        Collections.addAll(this.methodRequests, methodRequests);
    }

    public List<CalendarEvent> getCalendarEvents() {
        return reader.getCalendarEvents();
    }

    public boolean isLoaded() {
        return loaded;
    }

}
