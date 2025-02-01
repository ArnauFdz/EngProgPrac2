package data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StationIDTest {

    @Test
    void testValidStationID() {
        StationID stationID = new StationID("STN_123");
        assertEquals("STN_123", stationID.getId());
    }

    @Test
    void testNullID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new StationID(null);
        });
        assertEquals("L'ID de l'estació no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testEmptyID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new StationID("");
        });
        assertEquals("L'ID de l'estació no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testInvalidFormatID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new StationID("a@12");
        });
        assertEquals("L'ID de l'estació ha de tenir entre 5 i 20 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testTooShortID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new StationID("abcd");
        });
        assertEquals("L'ID de l'estació ha de tenir entre 5 i 20 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testTooLongID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new StationID("A".repeat(21));
        });
        assertEquals("L'ID de l'estació ha de tenir entre 5 i 20 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        StationID stationID1 = new StationID("STATION123");
        StationID stationID2 = new StationID("STATION123");
        StationID stationID3 = new StationID("DIFFERENT_ID");

        assertEquals(stationID1, stationID2);
        assertNotEquals(stationID1, stationID3);
        assertEquals(stationID1.hashCode(), stationID2.hashCode());
    }

    @Test
    void testToString() {
        StationID stationID = new StationID("STN_456");
        assertEquals("StationID{id='STN_456'}", stationID.toString());
    }
}
