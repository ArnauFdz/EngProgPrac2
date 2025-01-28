package micromobility;

import data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PMVehicleTest {
    private PMVehicle vehicle;
    private GeographicPoint initialLocation;

    @BeforeEach
    void setUp() {
        // Configura el vehicle i la ubicació inicial abans de cada test
        initialLocation = new GeographicPoint(41.3851f, 2.1734f);
        vehicle = new PMVehicle(new VehicleID("VH123456"), PMVState.Available, initialLocation);
    }

    @Test
    void testInitialState() {
        // Comprova l'estat i la ubicació inicial del vehicle
        assertEquals(PMVState.Available, vehicle.getState()); // Estat inicial hauria de ser Available
        assertEquals(initialLocation, vehicle.getLocation()); // Ubicació inicial hauria de coincidir
    }

    @Test
    void testGetVehicleID() {
        // Comprova que es retorna l'identificador correctament
        assertEquals("VH123456", vehicle.getVehicleID().getId());
    }

    @Test
    void testSetNotAvailable() {
        // Comprova que es pot establir l'estat del vehicle com NotAvailable
        vehicle.setNotAvailb();
        assertEquals(PMVState.NotAvailable, vehicle.getState());
    }

    @Test
    void testSetUnderWay() {
        // Comprova que es pot establir l'estat del vehicle com UnderWay
        vehicle.setUnderWay();
        assertEquals(PMVState.UnderWay, vehicle.getState());
    }

    @Test
    void testSetAvailb() {
        // Comprova que es pot establir l'estat del vehicle com Available
        vehicle.setAvailb();
        assertEquals(PMVState.Available, vehicle.getState());
    }

    @Test
    void testSetLocation() {
        // Comprova que es pot actualitzar la ubicació del vehicle
        GeographicPoint newLocation = new GeographicPoint(40.7128f, -74.0060f); // Nova ubicació
        vehicle.setLocation(newLocation);
        assertEquals(newLocation, vehicle.getLocation()); // Verifica que la ubicació s'ha actualitzat correctament
    }

    @Test
    void testSetLocation_Null() {
        // Comprova que es llença una excepció quan la ubicació és nul·la
        assertThrows(IllegalArgumentException.class, () -> vehicle.setLocation(null));
    }

    @Test
    void testConstructor_NullVehicleID() {
        // Comprova que el constructor llença una excepció si el VehicleID és nul
        assertThrows(IllegalArgumentException.class, () ->
                new PMVehicle(null, PMVState.Available, initialLocation)
        );
    }

    @Test
    void testConstructor_NullState() {
        // Comprova que el constructor llença una excepció si l'estat inicial és nul
        assertThrows(IllegalArgumentException.class, () ->
                new PMVehicle(new VehicleID("VH123456"), null, initialLocation)
        );
    }

    @Test
    void testConstructor_NullLocation() {
        // Comprova que el constructor llença una excepció si la ubicació inicial és nul·la
        assertThrows(IllegalArgumentException.class, () ->
                new PMVehicle(new VehicleID("VH123456"), PMVState.Available, null)
        );
    }
}
