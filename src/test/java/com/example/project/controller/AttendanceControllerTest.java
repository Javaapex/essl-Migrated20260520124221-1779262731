package com.example.project.controller;

import com.example.project.model.AttendanceRecord;
import com.example.project.model.AttendanceSoapData;
import com.example.project.service.AttendanceReportLogService;
import com.example.project.service.AttendanceService;
import com.example.project.service.EmailService;
import com.example.project.service.WeeklyAttendanceReportService;
import com.example.project.soap.SoapClient;
import com.example.project.soap.SoapParser;
import jakarta.xml.soap.SOAPMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttendanceControllerTest {

    private AttendanceService attendanceService;
    private SoapClient soapClient;
    private AttendanceController controller;
    private SOAPMessage soapMessage;

    @BeforeEach
    void setUp() {
        attendanceService = mock(AttendanceService.class);
        soapClient = mock(SoapClient.class);
        soapMessage = mock(SOAPMessage.class);
        controller = new AttendanceController(
                attendanceService,
                soapClient,
                mock(EmailService.class),
                mock(WeeklyAttendanceReportService.class),
                mock(AttendanceReportLogService.class)
        );
    }

    @Test
    void fetchAttendanceReturnsProcessedRecords() throws Exception {
        List<AttendanceSoapData> raw = List.of(new AttendanceSoapData());
        List<AttendanceRecord> records = List.of(new AttendanceRecord());
        when(soapClient.callSoap("2024-01-15", "2024-01-15")).thenReturn(soapMessage);

        try (MockedStatic<SoapParser> parser = mockStatic(SoapParser.class)) {
            parser.when(() -> SoapParser.parse(soapMessage)).thenReturn(raw);
            when(attendanceService.processSoapData(raw)).thenReturn(records);

            assertEquals(records, controller.fetchAttendance("2024-01-15"));
        }
    }

    @Test
    void getAttendanceReturnsErrorResponseWhenSoapFails() throws Exception {
        when(soapClient.callSoap(any(), any())).thenThrow(new RuntimeException("SOAP error"));

        ResponseEntity<Map<String, Object>> response = controller.getAttendance("2024-01-15", "daily");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("SOAP error", response.getBody().get("error"));
    }
}
