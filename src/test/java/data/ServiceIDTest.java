package data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceIDTest {

    @Test
    void testValidServiceID() {
        ServiceID serviceID = new ServiceID("Service_123");
        assertEquals("Service_123", serviceID.getId());
    }

    @Test
    void testNullID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID(null);
        });
        assertEquals("L'ID del servei no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testEmptyID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("");
        });
        assertEquals("L'ID del servei no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testInvalidFormatID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("a@12");
        });
        assertEquals("L'ID del servei ha de tenir entre 6 i 30 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testTooShortID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("abc");
        });
        assertEquals("L'ID del servei ha de tenir entre 6 i 30 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testTooLongID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("a".repeat(31));
        });
        assertEquals("L'ID del servei ha de tenir entre 6 i 30 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ServiceID serviceID1 = new ServiceID("Service123");
        ServiceID serviceID2 = new ServiceID("Service123");
        ServiceID serviceID3 = new ServiceID("DifferentService");

        assertEquals(serviceID1, serviceID2);
        assertNotEquals(serviceID1, serviceID3);
        assertEquals(serviceID1.hashCode(), serviceID2.hashCode());
    }

    @Test
    void testToString() {
        ServiceID serviceID = new ServiceID("Service_456");
        assertEquals("ServiceID{id='Service_456'}", serviceID.toString());
    }
}
