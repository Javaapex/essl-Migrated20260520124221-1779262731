# Test Plan: Migrated Java Spring Boot Attendance Project

---

## 1. Unit Test Inventory

### 1.1 `ProjectApplication`

| Class/Method | What to Assert |
|---|---|
| `main()` | Application context loads without errors; `@EnableScheduling` bean is registered; `@SpringBootApplication` triggers component scan |

```java
@SpringBootTest
class ProjectApplicationTest {
    @Test
    void contextLoads() { }

    @Test
    void schedulingIsEnabled() {
        // Assert ScheduledTaskHolder bean exists in context
    }
}
```

---

### 1.2 `AttendanceController`

| Method | What to Assert |
|---|---|
| `buildSummary(int count, String source)` | Returns `"1 Employee (Manual)"` when count=1, source="Manual" |
| `buildSummary(int count, String source)` | Returns `"5 Employees (Manual)"` when count=5, source="Manual" |
| `buildSummary(int count, String source)` | Returns correct label for non-Manual source |
| `buildSummary(int count, String source)` | Case-insensitive match: `"manual"`, `"MANUAL"` all pass |
| `sendAttendanceEmailHtml()` | Returns `200 OK` on valid input |
| `sendAttendanceEmailHtml()` | Returns `400` or `500` on null/invalid params |
| `sendAttendanceEmailHtml()` | Delegates to `EmailService` exactly once |

```java
@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    @InjectMocks AttendanceController controller;
    @Mock AttendanceService attendanceService;
    @Mock SoapClient soapClient;
    @Mock EmailService emailService;
    @Mock WeeklyAttendanceReportService weeklyReportService;
    @Mock AttendanceReportLogService reportLogService;

    @Test
    void buildSummary_singleEmployee_manual() {
        // Use reflection or package-private access
        // Assert: "1 Employee (Manual)"
    }

    @Test
    void buildSummary_multipleEmployees_manual() {
        // Assert: "5 Employees (Manual)"
    }

    @Test
    void buildSummary_caseInsensitive_manual() {
        // source = "MANUAL" → still appends "(Manual)"
    }

    @Test
    void buildSummary_nonManualSource() {
        // Assert label does not contain "(Manual)"
    }
}
```

---

### 1.3 `AttendanceCronPM`

| Method | What to Assert |
|---|---|
| `sendDailyEmail()` | Calls `attendanceController.sendAttendanceEmailHtml(null, null, null, "Cron")` exactly once |
| `sendDailyEmail()` | Exception is caught and logged; does NOT propagate |
| `sendDailyEmail()` | Cron expression `"0 0 21 * * *"` is valid and parseable |
| Constructor | Both dependencies injected correctly |

```java
@ExtendWith(MockitoExtension.class)
class AttendanceCronPMTest {

    @Mock WeeklyAttendanceReportService weeklyReportService;
    @Mock AttendanceController attendanceController;
    @InjectMocks AttendanceCronPM cronPM;

    @Test
    void sendDailyEmail_invokesController() {
        cronPM.sendDailyEmail();
        verify(attendanceController, times(1))
            .sendAttendanceEmailHtml(null, null, null, "Cron");
    }

    @Test
    void sendDailyEmail_exceptionDoesNotPropagate() {
        doThrow(new RuntimeException("SOAP failure"))
            .when(attendanceController)
            .sendAttendanceEmailHtml(any(), any(), any(), eq("Cron"));
        assertDoesNotThrow(() -> cronPM.sendDailyEmail());
    }

    @Test
    void cronExpression_isValid() {
        // Parse "0 0 21 * * *" with CronExpression.parse() (Spring 5.3+)
        assertDoesNotThrow(() ->
            CronExpression.parse("0 0 21 * * *"));
    }
}
```

---

### 1.4 Supporting Services (Stub Inventory)

| Class | Key Methods to Unit Test | Assertions |
|---|---|---|
| `AttendanceService` | `getRecords(date)` | Returns list; empty list on no data; throws on null date |
| `EmailService` | `sendHtmlEmail(to, subject, body)` | Correct MIME type; no NPE on empty body |
| `WeeklyAttendanceReportService` | `generateReport(weekStart)` | Correct date range; handles weekend edge cases |
| `AttendanceReportLogService` | `saveLog(log)` | Persists entity; returns saved ID |
| `SoapClient` | `sendRequest(message)` | Returns `SOAPMessage`; throws on timeout |
| `SoapParser` | `parse(SOAPMessage)` | Maps XML fields to `AttendanceSoapData`; handles malformed XML |

---

## 2. Automated Test Plan

### 2.1 Integration Tests

```
Framework: Spring Boot Test + Testcontainers (DB) + WireMock (SOAP)
```

| Test ID | Scenario | Endpoint / Component | Expected Result |
|---|---|---|---|
| INT-01 | Full attendance fetch via SOAP | `GET /api/attendance?date=today` | 200 OK; records returned |
| INT-02 | SOAP service unavailable | `GET /api/attendance` | Graceful error; 503 or fallback |
| INT-03 | Email triggered via API | `POST /api/attendance/send-email` | 200 OK; `EmailService` invoked |
| INT-04 | Report