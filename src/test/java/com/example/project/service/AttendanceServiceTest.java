package com.example.project.service;

import com.example.project.model.AttendanceRecord;
import com.example.project.model.AttendanceSoapData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceServiceTest {

    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceService();
    }

    @Test
    void processSoapDataGroupsInAndOutForSameUserAndDate() {
        AttendanceSoapData in = data("1", "Alice", "2025-11-25T09:00:00", "IN");
        AttendanceSoapData out = data("1", "Alice", "2025-11-25T17:30:00", "OUT");

        List<AttendanceRecord> result = attendanceService.processSoapData(List.of(out, in));

        assertEquals(1, result.size());
        AttendanceRecord record = result.get(0);
        assertEquals("1", record.getUserId());
        assertEquals("Alice", record.getUserName());
        assertEquals(LocalDate.of(2025, 11, 25), record.getDate());
        assertEquals(LocalDateTime.of(2025, 11, 25, 9, 0), record.getInTime());
        assertEquals(LocalDateTime.of(2025, 11, 25, 17, 30), record.getOutTime());
        assertEquals("8h 30m", record.getDuration());
    }

    @Test
    void processSoapDataSkipsInvalidEntries() {
        assertTrue(attendanceService.processSoapData(List.of(
                data(null, "Alice", "2025-11-25T09:00:00", "IN"),
                data("1", "Alice", "invalid", "IN")
        )).isEmpty());
    }

    private AttendanceSoapData data(String userId, String userName, String timestamp, String direction) {
        AttendanceSoapData data = new AttendanceSoapData();
        data.setUserId(userId);
        data.setUserName(userName);
        data.setTimeStamp(timestamp);
        data.setDirection(direction);
        return data;
    }
}
