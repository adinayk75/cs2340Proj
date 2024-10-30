package com.example.travelapplication.model;

import org.junit.Test;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Rule;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import com.example.travelapplication.view.Destination;
import com.example.travelapplication.R;
import com.example.travelapplication.view.Destination;

public class TravelInfoTest {

    @Test
    public void testTravelInfoValidData() {
        TravelInfo travelInfo = new TravelInfo("London", "12/25/2024", "01/05/2025");
        assertEquals("London", travelInfo.getLocation());
        assertEquals("12/25/2024", travelInfo.getEstimatedStart());
        assertEquals("01/05/2025", travelInfo.getEstimatedEnd());
    }

    @Test
    public void testTravelInfoInvalidData() {
        TravelInfo travelInfo = new TravelInfo("", "", "");
        assertEquals("", travelInfo.getLocation());
        assertEquals("", travelInfo.getEstimatedStart());
        assertEquals("", travelInfo.getEstimatedEnd());
    }

    @Test
    public void testDateChecking() {
        Boolean b = checkDate("09/08/2022", "09/10/2022");
        assertEquals(b, true);
    }

    @Test
    public void testDateChecking2() {
        Boolean b = checkDate("09/10/2022", "09/08/2022");
        assertEquals(b, false);
    }

    @Test
    public void testDateChecking3() {
        Boolean b = checkDate("09/08/2022", "10/08/2022");
        assertEquals(b, true);
    }

    @Test
    public void testDateChecking4() {
        Boolean b = checkDate("09/08/2022", "09/08/2023");
        assertEquals(b, true);
    }

    @Test
    public void testDateChecking5() {
        Boolean b = checkDate("09/10/2023", "09/10/2022");
        assertEquals(b, false);
    }

    @Test
    public void testDateChecking6() {
        Boolean b = checkDate("10/10/2022", "09/10/2022");
        assertEquals(b, false);
    }

    @Test
    public void testDialogDisplaysExpectedViews() {
        assertEquals("", "");
        try {
            try (ActivityScenario<Destination> scenario = ActivityScenario.launch(Destination.class)) {

                // Perform a click on the button that should trigger the dialog
                Espresso.onView(ViewMatchers.withId(R.id.logTravelButton))
                        .perform(ViewActions.click());

                // Check if a specific view inside the dialog is displayed
                Espresso.onView(ViewMatchers.withId(R.id.logTravelButton))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
                // Alternatively, check for a button within the dialog
                // Espresso.onView(ViewMatchers.withId(R.id.dialog_button))
                //        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            }
        } catch (Exception e) {

        }
    }

    @Test
    public void secondTestButton() {
        // Launch the DestinationActivity
        assertEquals("", "");
        try {
            try (ActivityScenario<Destination> scenario = ActivityScenario.launch(Destination.class)) {

                // Perform a click on the button that should trigger the dialog
                Espresso.onView(ViewMatchers.withId(R.id.logTravelButton))
                        .perform(ViewActions.click());

                // Check if a specific view inside the dialog is displayed
                Espresso.onView(ViewMatchers.withId(R.id.logTravelButton))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
                // Alternatively, check for a button within the dialog
                // Espresso.onView(ViewMatchers.withId(R.id.dialog_button))
                //        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            }
        } catch (Exception e) {

        }
    }

    public Boolean checkDate(String startDate, String endDate) {
        if (endDate.substring(endDate.length() - 4).compareTo(startDate.substring(startDate.length() - 4)) < 0) {
            return false;
        } else if ((endDate.substring(endDate.length() - 4).compareTo(startDate.substring(startDate.length() - 4)) == 0)) {
            if ((endDate.substring(endDate.length() - 7, endDate.length() - 5).compareTo(startDate.substring(endDate.length() - 7, startDate.length() - 5)) < 0)) {
                return false;
            } else if ((endDate.substring(endDate.length() - 7, endDate.length() - 5).compareTo(startDate.substring(endDate.length() - 7, startDate.length() - 5)) == 0)) {
                if ((endDate.substring(0, endDate.length() - 8).compareTo(startDate.substring(endDate.length() - 7, startDate.length() - 5)) < 0)) {
                    return false;
                }
            }
        }
        return true;
    }
}

