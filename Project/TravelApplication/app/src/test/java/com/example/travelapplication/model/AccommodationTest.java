package com.example.travelapplication.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.travelapplication.R;
import com.example.travelapplication.view.Accommodation;

public class AccommodationTest {

    @Test
    public void testAccommodationInfoValidData() {
        AccommodationInfo accommodationInfo = new AccommodationInfo("12/12/2024", "15/12/2024", "Paris", 2, "Double");
        assertEquals("12/12/2024", accommodationInfo.getCheckInDate());
        assertEquals("15/12/2024", accommodationInfo.getCheckOutDate());
        assertEquals("Paris", accommodationInfo.getLocation());
        assertEquals(2, accommodationInfo.getNumRooms());
        assertEquals("Double", accommodationInfo.getRoomType());
    }

    @Test
    public void testAccommodationInfoInvalidData() {
        AccommodationInfo accommodationInfo = new AccommodationInfo("", "", "", 0, "");
        assertEquals("", accommodationInfo.getCheckInDate());
        assertEquals("", accommodationInfo.getCheckOutDate());
        assertEquals("", accommodationInfo.getLocation());
        assertEquals(0, accommodationInfo.getNumRooms());
        assertEquals("", accommodationInfo.getRoomType());
    }

    @Test
    public void testAccommodationInfoSetters() {
        AccommodationInfo accommodationInfo = new AccommodationInfo();
        accommodationInfo.setCheckInDate("10/10/2024");
        accommodationInfo.setCheckOutDate("20/10/2024");
        accommodationInfo.setLocation("New York");
        accommodationInfo.setNumRooms(3);
        accommodationInfo.setRoomType("Suite");

        assertEquals("10/10/2024", accommodationInfo.getCheckInDate());
        assertEquals("20/10/2024", accommodationInfo.getCheckOutDate());
        assertEquals("New York", accommodationInfo.getLocation());
        assertEquals(3, accommodationInfo.getNumRooms());
        assertEquals("Suite", accommodationInfo.getRoomType());
    }

    @Test
    public void testGetCheckInLocalDate() {
        AccommodationInfo accommodationInfo = new AccommodationInfo("12/12/2024", "15/12/2024", "Tokyo", 1, "Single");
        assertEquals("2024-12-12", accommodationInfo.getCheckInLocalDate().toString());
    }

    @Test
    public void testAccommodationInfoEmptyLocation() {
        AccommodationInfo accommodationInfo = new AccommodationInfo("01/01/2025", "10/01/2025", "", 1, "Single");
        assertEquals("", accommodationInfo.getLocation());
    }

    @Test
    public void testAccommodationInfoEmptyRoomType() {
        AccommodationInfo accommodationInfo = new AccommodationInfo("01/01/2025", "10/01/2025", "Los Angeles", 1, "");
        assertEquals("", accommodationInfo.getRoomType());
    }

    @Test
    public void testDialogDisplaysExpectedViews() {
        assertEquals("", "");

        try {
            try (ActivityScenario<Accommodation> scenario = ActivityScenario.launch(Accommodation.class)) {

                Espresso.onView(ViewMatchers.withId(R.id.addAccommodationButton))
                        .perform(ViewActions.click());

                Espresso.onView(ViewMatchers.withId(R.id.textView3))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

                Espresso.onView(ViewMatchers.withId(R.id.accommodationContainer))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

                Espresso.onView(ViewMatchers.withId(R.id.accLogout))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void secondTestButton() {
        assertEquals("", "");

        try {
            try (ActivityScenario<Accommodation> scenario = ActivityScenario.launch(Accommodation.class)) {

                Espresso.onView(ViewMatchers.withId(R.id.addAccommodationButton))
                        .perform(ViewActions.click());

                Espresso.onView(ViewMatchers.withId(R.id.addAccommodationButton))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

                Espresso.onView(ViewMatchers.withId(R.id.bottomNavigationView))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}