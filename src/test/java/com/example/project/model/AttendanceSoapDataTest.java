package com.example.project.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceSoapDataTest {

    @Test
    void gettersAndSettersRoundTrip() {
        AttendanceSoapData data = new AttendanceSoapData();

        data.setUserId("123");
        data.setUserName("Alice");
        data.setTimeStamp("2025-11-25T10:41:50");
        data.setDirection("IN");

        assertEquals("123", data.getUserId());
        assertEquals("Alice", data.getUserName());
        assertEquals("2025-11-25T10:41:50", data.getTimeStamp());
        assertEquals("IN", data.getDirection());
    }

    @Test
    void timeHelpersParseTimestamp() {
        AttendanceSoapData data = new AttendanceSoapData();
        data.setTimeStamp("2025-11-25T10:41:50");

        assertEquals(LocalDateTime.of(2025, 11, 25, 10, 41, 50), data.getTimeAsDateTime());
        assertEquals("10:41", data.getTimeOnly());
    }
}
