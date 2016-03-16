package com.unit5app.activities;

/**
 * A basic loading Activity that just gets rid of the navigation drawer.
 */
public class LoadingActivity extends BaseActivity {
    @Override
    protected boolean useDrawerToggle() {
        return false;
    } //replaces the drawer with a back arrow in the top left.
}
