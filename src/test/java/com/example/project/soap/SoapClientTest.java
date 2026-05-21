package com.example.project.soap;

import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SoapClientTest {

    @Test
    void callSoapBuildsExpectedRequestAndReturnsResponse() throws Exception {
        SoapClient soapClient = new SoapClient();
        SOAPConnectionFactory factory = mock(SOAPConnectionFactory.class);
        SOAPConnection connection = mock(SOAPConnection.class);
        SOAPMessage response = mock(SOAPMessage.class);
        ArgumentCaptor<SOAPMessage> requestCaptor = ArgumentCaptor.forClass(SOAPMessage.class);

        when(factory.createConnection()).thenReturn(connection);
        when(connection.call(requestCaptor.capture(), anyString())).thenReturn(response);
        doNothing().when(response).writeTo(any());

        try (MockedStatic<SOAPConnectionFactory> mocked = Mockito.mockStatic(SOAPConnectionFactory.class)) {
            mocked.when(SOAPConnectionFactory::newInstance).thenReturn(factory);

            SOAPMessage result = soapClient.callSoap("2024-01-01", "2024-01-31");

            assertSame(response, result);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            requestCaptor.getValue().writeTo(out);
            String xml = out.toString();
            assertTrue(xml.contains("2024-01-01 00:00:00"));
            assertTrue(xml.contains("2024-01-31 23:59:59"));
            assertTrue(xml.contains("JJA1251900136"));
        }
    }
}
