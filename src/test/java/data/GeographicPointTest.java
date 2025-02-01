// Paquete: data
package data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests per a GeographicPoint.
 */
public class GeographicPointTest {

    @Test
    void testValidGeographicPoint() {
        GeographicPoint point = new GeographicPoint(45.0f, 90.0f);
        assertEquals(45.0f, point.getLatitude());
        assertEquals(90.0f, point.getLongitude());
    }

    @Test
    void testInvalidLatitude() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GeographicPoint(100.0f, 50.0f);
        });
        assertEquals("La latitud ha d'estar entre -90 y 90.", exception.getMessage());
    }

    @Test
    void testInvalidLongitude() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GeographicPoint(45.0f, 200.0f);
        });
        assertEquals("La longitud ha d'estar entre -180 y 180.", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        GeographicPoint point1 = new GeographicPoint(45.0f, 90.0f);
        GeographicPoint point2 = new GeographicPoint(45.0f, 90.0f);
        GeographicPoint point3 = new GeographicPoint(-45.0f, -90.0f);

        assertEquals(point1, point2);
        assertNotEquals(point1, point3);
        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    void testToString() {
        GeographicPoint point = new GeographicPoint(12.34f, 56.78f);
        assertEquals("Geographic point {latitude='12.34', longitude='56.78'}", point.toString());
    }
}