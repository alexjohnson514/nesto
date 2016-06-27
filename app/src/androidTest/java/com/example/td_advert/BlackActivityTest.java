package com.example.td_advert;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

public class BlackActivityTest extends ActivityInstrumentationTestCase2<BlackActivity> {

    private Solo solo;

    public BlackActivityTest() {
        super(BlackActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testClick() throws Exception {
        solo.clickOnView(solo.getView(R.id.welcomeOnBoard));
    }
}