package com.example.project.cron;

import com.example.project.controller.AttendanceController;
import com.example.project.service.WeeklyAttendanceReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttendanceCronPMTest {

    private WeeklyAttendanceReportService weeklyReportService;
    private AttendanceController attendanceController;
    private AttendanceCronPM attendanceCronPM;

    @BeforeEach
    void setUp() {
        weeklyReportService = mock(WeeklyAttendanceReportService.class);
        attendanceController = mock(AttendanceController.class);
        attendanceCronPM = new AttendanceCronPM(weeklyReportService, attendanceController);
    }

    @Test
    void sendDailyEmailDelegatesToController() {
        attendanceCronPM.sendDailyEmail();

        verify(attendanceController).sendAttendanceEmailHtml(null, null, null, "Cron");
    }

    @Test
    void sendWeeklyEmailDelegatesToControllerWithPreviousMonday() {
        LocalDate currentMonday = LocalDate.of(2024, 3, 11);
        when(weeklyReportService.getWeekStartMonday(any(LocalDate.class))).thenReturn(currentMonday);

        attendanceCronPM.sendWeeklyEmail();

        verify(attendanceController).sendWeeklyAttendanceEmailHtml("2024-03-04", "Cron");
    }
}
