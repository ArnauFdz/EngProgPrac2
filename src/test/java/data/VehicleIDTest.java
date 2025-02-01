package data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleIDTest {

    @Test
    void testValidVehicleID() {
        VehicleID vehicle = new VehicleID("ABC123");
        assertEquals("ABC123", vehicle.getId());
    }

    @Test
    void testNullVehicleID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new VehicleID(null);
        });
        assertEquals("L'ID del vehicle no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testEmptyVehicleID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new VehicleID("");
        });
        assertEquals("L'ID del vehicle no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testInvalidFormatVehicleID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new VehicleID("abc-123");
        });
        assertEquals("L'ID del vehicle ha de tenir entre 6 i 15 caràcters alfanumèrics.", exception.getMessage());
    }

    @Test
    void testTooShortVehicleID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new VehicleID("A123");
        });
        assertEquals("L'ID del vehicle ha de tenir entre 6 i 15 caràcters alfanumèrics.", exception.getMessage());
    }

    @Test
    void testTooLongVehicleID() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new VehicleID("A".repeat(16));
        });
        assertEquals("L'ID del vehicle ha de tenir entre 6 i 15 caràcters alfanumèrics.", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        VehicleID vehicle1 = new VehicleID("XYZ789");
        VehicleID vehicle2 = new VehicleID("XYZ789");
        VehicleID vehicle3 = new VehicleID("ABC123");

        assertEquals(vehicle1, vehicle2);
        assertNotEquals(vehicle1, vehicle3);
        assertEquals(vehicle1.hashCode(), vehicle2.hashCode());
    }

    @Test
    void testToString() {
        VehicleID vehicle = new VehicleID("CAR456");
        assertEquals("VehicleID{id='CAR456'}", vehicle.toString());
    }
}
