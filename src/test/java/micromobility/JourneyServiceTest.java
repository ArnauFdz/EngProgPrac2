package micromobility;

import data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class JourneyServiceTest {

    private JourneyService journey;
    private ServiceID serviceID;
    private UserAccount user;
    private VehicleID vehicle;
    private StationID startStation;

    @BeforeEach
    void setUp() {
        serviceID = new ServiceID("JNY_001");
        user = new UserAccount("User123");
        vehicle = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        startStation = new StationID("ST_001",location);
        journey = new JourneyService(serviceID, user, vehicle, startStation);
    }

    @Test
    void testConstructor() {
        assertEquals(serviceID, journey.getServiceID());
        assertEquals(user, journey.getUser());
        assertEquals(vehicle, journey.getVehicle());
        assertEquals(startStation, journey.getStartStation());
        assertFalse(journey.isActive());
    }

    @Test
    void testSetServiceInit() {
        journey.setServiceInit();

        assertTrue(journey.isActive());
        assertNotNull(journey.getStartTime());
        assertTrue(journey.getStartTime().isBefore(LocalDateTime.now()) || journey.getStartTime().isEqual(LocalDateTime.now()));
    }

    @Test
    void testSetServiceFinish() {
        // Iniciem el servei
        journey.setServiceInit();
        journey.setServiceFinish();

        // Comprovar que el servei no està actiu
        assertFalse(journey.isActive(), "El servei hauria de ser inactiu després de finalitzar-lo.");

        // Comprovar que el temps final s'ha establert
        assertNotNull(journey.getEndTime(), "El temps final no hauria de ser nul després de finalitzar el servei.");

        // Permet una tolerància de temps per evitar errors deguts a desajustos
        LocalDateTime now = LocalDateTime.now();
        assertTrue(journey.getEndTime().isBefore(now.plusSeconds(1)) && journey.getEndTime().isAfter(now.minusSeconds(1)),
                "El temps final hauria d'estar dins del rang actual.");
    }


    @Test
    void testSetEndStation() {
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        StationID endStation = new StationID("ST_002",location);
        journey.setEndStation(endStation);

        assertEquals(endStation, journey.getEndStation());
    }

    @Test
    void testSetEndStation_NullValue() {
        assertThrows(IllegalArgumentException.class, () -> journey.setEndStation(null));
    }

    @Test
    void testSetServiceInit_AlreadyActive() {
        journey.setServiceInit();
        assertThrows(IllegalStateException.class, journey::setServiceInit);
    }

    @Test
    void testSetServiceFinish_NotActive() {
        assertThrows(IllegalStateException.class, journey::setServiceFinish);
    }

    @Test
    void testGetters() {
        assertEquals(serviceID, journey.getServiceID());
        assertEquals(user, journey.getUser());
        assertEquals(vehicle, journey.getVehicle());
        assertEquals(startStation, journey.getStartStation());
        assertNull(journey.getEndStation());
        assertNull(journey.getStartTime());
        assertNull(journey.getEndTime());
        assertFalse(journey.isActive());
    }
}
