package com.unit5app;

import android.os.AsyncTask;

/**
 * @author Andrew and Ben
 * @version 2/26/16
 */
public class UpdateManager{
    private int updateTime; // determined by settings menu, (default 30min)

    public UpdateManager(Object... objects) {
        new QueryUpdatesTask().execute(objects);
    }

    private class QueryUpdatesTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... objects) {
            /* While true do updates every updateTime */
            while (true) {
                for(Object o : objects) {
                    // Trigger updates
                }
                // Wait updateTime
            }
        }
    }
}
