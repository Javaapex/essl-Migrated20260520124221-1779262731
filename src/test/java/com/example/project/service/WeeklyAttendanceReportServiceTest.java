package com.example.project.service;

import com.example.project.soap.SoapClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WeeklyAttendanceReportServiceTest {

    private WeeklyAttendanceReportService service;

    @BeforeEach
    void setUp() {
        service = new WeeklyAttendanceReportService(
                mock(AttendanceService.class),
                mock(SoapClient.class),
                mock(EmailService.class)
        );
    }

    @Test
    void getWeekStartMondayReturnsPreviousOrSameMonday() {
        assertEquals(LocalDate.of(2024, 3, 11), service.getWeekStartMonday(LocalDate.of(2024, 3, 13)));
        assertEquals(DayOfWeek.MONDAY, service.getWeekStartMonday(LocalDate.of(2024, 3, 11)).getDayOfWeek());
    }

    @Test
    void getWeekRangeForSelectedDateReturnsMondayThroughSunday() {
        WeeklyAttendanceReportService.WeekRange range =
                service.getWeekRangeForSelectedDate(LocalDate.of(2024, 3, 13), true);

        assertEquals(LocalDate.of(2024, 3, 11), range.getStart());
        assertEquals(LocalDate.of(2024, 3, 17), range.getEnd());
    }
}
