package data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per comprovar les funcionalitats i excepcions del constructor de StationID.
 */
class StationIDTest {

    private StationID validID;
    private GeographicPoint validLocation;

    @BeforeEach
    void setUp() {
        // Configuració d'objectes abans de cada test
        validLocation = new GeographicPoint(41.3851f, 2.1734f); // Ubicació vàlida
        validID = new StationID("ST-12345", validLocation);
    }

    @Test
    void testValidID() {
        // Comprova que el getter de l'ID funciona correctament
        assertEquals("ST-12345", validID.getId());
    }

    @Test
    void testValidLocation() {
        // Comprova que el getter de la ubicació funciona correctament
        assertEquals(validLocation, validID.getUbicacio());
    }

    @Test
    void testInvalidID_Null() {
        // Comprova que es llança una excepció per un ID nul
        assertThrows(IllegalArgumentException.class, () -> new StationID(null, validLocation),
                "S'hauria d'haver llançat una excepció per un ID nul.");
    }

    @Test
    void testInvalidLocation_Null() {
        // Comprova que es llança una excepció per una ubicació nul·la
        assertThrows(IllegalArgumentException.class, () -> new StationID("ST-12345", null),
                "S'hauria d'haver llançat una excepció per una ubicació nul·la.");
    }

    @Test
    void testInvalidID_Empty() {
        // Comprova que es llança una excepció per un ID buit
        assertThrows(IllegalArgumentException.class, () -> new StationID("", validLocation),
                "S'hauria d'haver llançat una excepció per un ID buit.");
    }

    @Test
    void testInvalidID_Whitespace() {
        // Comprova que es llança una excepció per un ID amb només espais
        assertThrows(IllegalArgumentException.class, () -> new StationID("   ", validLocation),
                "S'hauria d'haver llançat una excepció per un ID només amb espais.");
    }

    @Test
    void testInvalidID_ShortFormat() {
        // Comprova que es llança una excepció per un format massa curt
        assertThrows(IllegalArgumentException.class, () -> new StationID("123", validLocation),
                "S'hauria d'haver llançat una excepció per un ID massa curt.");
    }

    @Test
    void testInvalidID_LongFormat() {
        // Comprova que es llança una excepció per un format massa llarg
        assertThrows(IllegalArgumentException.class, () -> new StationID("ABCDEFGHIJKLMN-123456789", validLocation),
                "S'hauria d'haver llançat una excepció per un ID massa llarg.");
    }

    @Test
    void testInvalidID_InvalidCharacters() {
        // Comprova que es llança una excepció per caràcters no permesos
        assertThrows(IllegalArgumentException.class, () -> new StationID("ID#123!", validLocation),
                "S'hauria d'haver llançat una excepció per caràcters no vàlids.");
    }

    @Test
    void testEqualsAndHashCode() {
        // Comprova equals i hashCode
        StationID sameID = new StationID("ST-12345", validLocation);
        GeographicPoint differentLocation = new GeographicPoint(41.3964f, 2.1904f);
        StationID differentID = new StationID("ST-67890", differentLocation);

        assertEquals(validID, sameID, "Els IDs haurien de ser iguals.");
        assertNotEquals(validID, differentID, "Els IDs no haurien de ser iguals.");
        assertEquals(validID.hashCode(), sameID.hashCode(), "Els hashCodes haurien de coincidir.");
    }
}
