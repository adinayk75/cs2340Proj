package com.example.travelapplication.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

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

}
