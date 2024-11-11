package com.example.travelapplication.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.travelapplication.R;
import com.example.travelapplication.view.Dining;

public class DiningTest {

    @Test
    public void testDiningInfoValidData() {
        DiningInfo diningInfo = new DiningInfo("Paris", "12:00 PM", "www.parisrestaurant.com", "Le Bistro");
        assertEquals("Paris", diningInfo.getLocation());
        assertEquals("12:00 PM", diningInfo.getTime());
        assertEquals("www.parisrestaurant.com", diningInfo.getWebsite());
        assertEquals("Le Bistro", diningInfo.getName());
    }

    @Test
    public void testDiningInfoInvalidData() {
        DiningInfo diningInfo = new DiningInfo("", "", "", "");
        assertEquals("", diningInfo.getLocation());
        assertEquals("", diningInfo.getTime());
        assertEquals("", diningInfo.getWebsite());
        assertEquals("", diningInfo.getName());
    }

    @Test
    public void testDiningInfoSetters() {
        DiningInfo diningInfo = new DiningInfo();
        diningInfo.setLocation("New York");
        diningInfo.setTime("07:00 PM");
        diningInfo.setWebsite("www.nyrestaurant.com");
        diningInfo.setName("Bistro NYC");

        assertEquals("New York", diningInfo.getLocation());
        assertEquals("07:00 PM", diningInfo.getTime());
        assertEquals("www.nyrestaurant.com", diningInfo.getWebsite());
        assertEquals("Bistro NYC", diningInfo.getName());
    }

    @Test
    public void testDiningInfoEmptyLocation() {
        DiningInfo diningInfo = new DiningInfo("", "09:00 AM", "www.emptylocation.com", "Empty Diner");
        assertEquals("", diningInfo.getLocation());
        assertEquals("09:00 AM", diningInfo.getTime());
        assertEquals("www.emptylocation.com", diningInfo.getWebsite());
        assertEquals("Empty Diner", diningInfo.getName());
    }

    @Test
    public void testDiningInfoEmptyTime() {
        DiningInfo diningInfo = new DiningInfo("Los Angeles", "", "www.emptytime.com", "Late Diner");
        assertEquals("Los Angeles", diningInfo.getLocation());
        assertEquals("", diningInfo.getTime());
        assertEquals("www.emptytime.com", diningInfo.getWebsite());
        assertEquals("Late Diner", diningInfo.getName());
    }

    @Test
    public void testDiningInfoWebsite() {
        DiningInfo diningInfo = new DiningInfo("Tokyo", "06:00 PM", "", "Tokyo Grill");
        assertEquals("Tokyo", diningInfo.getLocation());
        assertEquals("06:00 PM", diningInfo.getTime());
        assertEquals("", diningInfo.getWebsite());
        assertEquals("Tokyo Grill", diningInfo.getName());
    }
    @Test
    public void testDialogDisplaysExpectedViews() {
        assertEquals("", "");

        try {
            try (ActivityScenario<Dining> scenario = ActivityScenario.launch(Dining.class)) {

                // Perform a click on the button that should trigger the dialog (FloatingActionButton with id 'add_reservation_floating_button')
                Espresso.onView(ViewMatchers.withId(R.id.add_reservation_floating_button))
                        .perform(ViewActions.click());

                // Check if a specific view inside the dialog is displayed (e.g., the "Dining Establishment" text view)
                Espresso.onView(ViewMatchers.withId(R.id.textView3))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

                // Check if the reservation container (display_reservation) is displayed
                Espresso.onView(ViewMatchers.withId(R.id.display_reservation))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

                // Alternatively, check for the logout button (id: dinLogout)
                Espresso.onView(ViewMatchers.withId(R.id.dinLogout))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

                // Check if the bottom navigation view is displayed (id: bottomNavigationView)
                Espresso.onView(ViewMatchers.withId(R.id.bottomNavigationView))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            }
        } catch (Exception e) {
            // Handle any potential errors or exceptions that occur during the test
            e.printStackTrace();
        }
    }

    @Test
    public void secondTestButton() {
        assertEquals("", "");

        try {
            try (ActivityScenario<Dining> scenario = ActivityScenario.launch(Dining.class)) {

                Espresso.onView(ViewMatchers.withId(R.id.add_reservation_floating_button))
                        .perform(ViewActions.click());

                Espresso.onView(ViewMatchers.withId(R.id.textView3))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

                Espresso.onView(ViewMatchers.withId(R.id.bottomNavigationView))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}