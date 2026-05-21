package com.example.project.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceRecordTest {

    @Test
    void gettersAndSettersRoundTripAndDurationUpdates() {
        AttendanceRecord record = new AttendanceRecord();
        LocalDate date = LocalDate.of(2024, 1, 15);
        LocalDateTime in = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime out = LocalDateTime.of(2024, 1, 15, 15, 4);

        record.setUserId("123");
        record.setUserName("John Doe");
        record.setDate(date);
        record.setInTime(in);
        record.setOutTime(out);

        assertEquals("123", record.getUserId());
        assertEquals("John Doe", record.getUserName());
        assertEquals(date, record.getDate());
        assertEquals(in, record.getInTime());
        assertEquals(out, record.getOutTime());
        assertEquals("6h 4m", record.getDuration());
    }

    @Test
    void durationRemainsNullUntilBothTimesArePresent() {
        AttendanceRecord record = new AttendanceRecord();

        record.setInTime(LocalDateTime.of(2024, 1, 15, 9, 0));

        assertNull(record.getDuration());
    }
}
