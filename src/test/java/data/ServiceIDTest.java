// Paquete: data
package data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests per a ServiceID.
 */
public class ServiceIDTest {

    @Test
    void testIDNul() {
        // Test que verifica que es llença una excepció per un ID nul
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID(null);
        });
        assertEquals("L'ID del servei no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testIDBuit() {
        // Test que verifica que es llença una excepció per un ID buit
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("");
        });
        assertEquals("L'ID del servei no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testIDEspais() {
        // Test que verifica que es llença una excepció per un ID amb només espais
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("   ");
        });
        assertEquals("L'ID del servei no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testIDMassaCurt() {
        // Test que verifica que es llença una excepció per un ID massa curt
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("abc");
        });
        assertEquals("L'ID del servei ha de tenir entre 6 i 30 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testIDMassaLlarg() {
        // Test que verifica que es llença una excepció per un ID massa llarg
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("abcdefghijklmnopqrstuvwxyz1234567890");
        });
        assertEquals("L'ID del servei ha de tenir entre 6 i 30 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testIDAmbCaràctersInvàlids() {
        // Test que verifica que es llença una excepció per un ID amb caràcters no vàlids
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceID("invalid@id");
        });
        assertEquals("L'ID del servei ha de tenir entre 6 i 30 caràcters alfanumèrics, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testIDValid() {
        // Test que verifica que no es llença cap excepció amb un ID vàlid
        ServiceID serviceID = new ServiceID("valid_id123");
        assertEquals("valid_id123", serviceID.getId());
    }
}