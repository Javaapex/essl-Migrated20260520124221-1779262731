package com.example.project.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceReportLogTest {

    @Test
    void gettersAndSettersRoundTrip() {
        AttendanceReportLog log = new AttendanceReportLog();
        LocalDate date = LocalDate.of(2024, 6, 15);

        log.setId(42L);
        log.setReportDate(date);
        log.setType("Daily");
        log.setSummary("18 Employees");
        log.setStatus("Success");

        assertEquals(42L, log.getId());
        assertEquals(date, log.getReportDate());
        assertEquals("Daily", log.getType());
        assertEquals("18 Employees", log.getSummary());
        assertEquals("Success", log.getStatus());
    }

    @Test
    void defaultsAreNull() {
        AttendanceReportLog log = new AttendanceReportLog();

        assertNull(log.getId());
        assertNull(log.getReportDate());
        assertNull(log.getType());
        assertNull(log.getSummary());
        assertNull(log.getStatus());
    }
}
