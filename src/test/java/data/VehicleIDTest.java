package data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleIDTest {

    private VehicleID validID;

    @BeforeEach
    void setUp() {
        // Configura un objecte VehicleID vàlid abans de cada test
        validID = new VehicleID("VH123456");
    }

    @Test
    void testValidID() {
        // Comprova que el mètode getId retorna el valor correcte de l'ID
        assertEquals("VH123456", validID.getId());
    }

    @Test
    void testInvalidID_Null() {
        // Comprova que es llença una excepció quan l'ID és nul
        assertThrows(IllegalArgumentException.class, () -> new VehicleID(null));
    }

    @Test
    void testInvalidID_Empty() {
        // Comprova que es llença una excepció quan l'ID és buit
        assertThrows(IllegalArgumentException.class, () -> new VehicleID(""));
    }

    @Test
    void testInvalidID_Format() {
        // Comprova que es llença una excepció quan l'ID no compleix amb el format esperat (massa curt)
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("ABC"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Configura dos objectes VehicleID amb el mateix ID i un altre amb un ID diferent
        VehicleID sameAsValidID = new VehicleID("VH123456");
        VehicleID differentID = new VehicleID("VH789012");

        // Comprova que dos objectes amb el mateix ID són considerats iguals
        assertEquals(validID, sameAsValidID);
        // Comprova que dos objectes amb IDs diferents no són considerats iguals
        assertNotEquals(validID, differentID);
        // Comprova que els hash codes d'objectes iguals són els mateixos
        assertEquals(validID.hashCode(), sameAsValidID.hashCode());
    }
}
