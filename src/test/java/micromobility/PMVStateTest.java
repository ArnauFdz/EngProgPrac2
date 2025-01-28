package micromobility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PMVStateTest {

    @Test
    void testEnumValues() {
        // Comprova que existeixen els valors esperats
        assertEquals(PMVState.Available, PMVState.valueOf("Available"));
        assertEquals(PMVState.NotAvailable, PMVState.valueOf("NotAvailable"));
        assertEquals(PMVState.UnderWay, PMVState.valueOf("UnderWay"));
        assertEquals(PMVState.TemporaryParking, PMVState.valueOf("TemporaryParking"));

    }

    @Test
    void testEnumSize() {
        // Verifica que hi ha exactament 3 valors a l'enumeració
        assertEquals(4, PMVState.values().length);
    }

    @Test
    void testEnumToString() {
        // Comprova la representació en forma de cadena dels valors
        assertEquals("Available", PMVState.Available.toString());
        assertEquals("NotAvailable", PMVState.NotAvailable.toString());
        assertEquals("UnderWay", PMVState.UnderWay.toString());
        assertEquals("TemporaryParking",  PMVState.TemporaryParking.toString());
    }
}
