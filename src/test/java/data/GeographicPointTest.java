// Paquete: data
package data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests per a GeographicPoint.
 */
public class GeographicPointTest {

    @Test
    void testLatitudForaDeRangPerSobre() {
        // Test que verifica que es llença una excepció per latitud > 90
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GeographicPoint(91.0f, 0.0f);
        });
        assertEquals("La latitud ha d'estar entre -90 y 90.", exception.getMessage());
    }

    @Test
    void testLatitudForaDeRangPerSota() {
        // Test que verifica que es llença una excepció per latitud < -90
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GeographicPoint(-91.0f, 0.0f);
        });
        assertEquals("La latitud ha d'estar entre -90 y 90.", exception.getMessage());
    }

    @Test
    void testLongitudForaDeRangPerSobre() {
        // Test que verifica que es llença una excepció per longitud > 180
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GeographicPoint(0.0f, 181.0f);
        });
        assertEquals("La longitud ha d'estar entre -180 y 180.", exception.getMessage());
    }

    @Test
    void testLongitudForaDeRangPerSota() {
        // Test que verifica que es llença una excepció per longitud < -180
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GeographicPoint(0.0f, -181.0f);
        });
        assertEquals("La longitud ha d'estar entre -180 y 180.", exception.getMessage());
    }

    @Test
    void testLatitudYLongitudCorrectes() {
        // Test que verifica que no es llença cap excepció amb valors vàlids
        GeographicPoint point = new GeographicPoint(45.0f, 90.0f);
        assertEquals(45.0f, point.getLatitude());
        assertEquals(90.0f, point.getLongitude());
    }
}