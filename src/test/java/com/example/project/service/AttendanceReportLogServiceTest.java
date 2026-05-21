package com.example.project.service;

import com.example.project.model.AttendanceReportLog;
import com.example.project.repository.AttendanceReportLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceReportLogServiceTest {

    @Mock
    private AttendanceReportLogRepository repository;

    @InjectMocks
    private AttendanceReportLogService service;

    private AttendanceReportLog sampleLog;

    @BeforeEach
    void setUp() {
        sampleLog = new AttendanceReportLog();
    }

    @Test
    void save_shouldReturnSavedLog_whenValidLogProvided() {
        when(repository.save(sampleLog)).thenReturn(sampleLog);

        AttendanceReportLog result = service.save(sampleLog);

        assertNotNull(result);
        assertEquals(sampleLog, result);
        verify(repository, times(1)).save(sampleLog);
    }

    @Test
    void save_shouldDelegateToRepository_andReturnRepositoryResult() {
        AttendanceReportLog anotherLog = new AttendanceReportLog();
        when(repository.save(sampleLog)).thenReturn(anotherLog);

        AttendanceReportLog result = service.save(sampleLog);

        assertNotNull(result);
        assertEquals(anotherLog, result);
        verify(repository).save(sampleLog);
    }

    @Test
    void save_shouldCallRepositorySaveExactlyOnce() {
        when(repository.save(any(AttendanceReportLog.class))).thenReturn(sampleLog);

        service.save(sampleLog);

        verify(repository, times(1)).save(sampleLog);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void save_shouldPropagateException_whenRepositoryThrows() {
        when(repository.save(any(AttendanceReportLog.class)))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.save(sampleLog));

        assertEquals("Database error", exception.getMessage());
        verify(repository, times(1)).save(sampleLog);
    }

    @Test
    void findAll_shouldReturnListOfLogs_whenLogsExist() {
        AttendanceReportLog log1 = new AttendanceReportLog();
        AttendanceReportLog log2 = new AttendanceReportLog();
        List<AttendanceReportLog> expectedLogs = Arrays.asList(log1, log2);

        when(repository.findAll()).thenReturn(expectedLogs);

        List<AttendanceReportLog> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedLogs, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoLogsExist() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<AttendanceReportLog> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findAll_shouldDelegateToRepository_andReturnExactRepositoryResult() {
        List<AttendanceReportLog> expectedLogs = Collections.singletonList(sampleLog);
        when(repository.findAll()).thenReturn(expectedLogs);

        List<AttendanceReportLog> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleLog, result.get(0));
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAll_shouldPropagateException_whenRepositoryThrows() {
        when(repository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.findAll());

        assertEquals("Database connection failed", exception.getMessage());
        verify(repository, times(1)).findAll();
    }
}