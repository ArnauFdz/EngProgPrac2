package services;
import micromobility.*;
import data.*;
import exceptions.*;
import micromobility.JourneyService;
import micromobility.payment.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.ArduinoMicroControllerImpl;
import services.smartfeatures.QRDecoderImpl;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    private ServerImp simulatedServer;

    @BeforeEach

    void setUp() {
        simulatedServer = new ServerImp();
        simulatedServer.setAvailableVehicles(Set.of(
                new VehicleID("VH123456"),
                new VehicleID("VH654321")));
    }

    @Test
    void testCheckPMVAvail_Success()  {
        VehicleID vehicleID = new VehicleID("VH123456");
        assertDoesNotThrow(() -> simulatedServer.checkPMVAvail(vehicleID));
    }

    @Test
    void testCheckPMVAvail_UnavailableVehicle()  {
        VehicleID unavailableVehicle = new VehicleID("VH999999");
        assertThrows(PMVNotAvailException.class, () -> simulatedServer.checkPMVAvail(unavailableVehicle));
    }



    @Test
    void testRegisterPairing_Success()  {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        LocalDateTime date = LocalDateTime.now();
        StationID stationID = new StationID("ST_001", location);

        assertDoesNotThrow(() -> simulatedServer.registerPairing(user, vehicleID, stationID, location, date));
    }

    @Test
    void testRegisterPairing_InvalidArgs() {
        UserAccount user = null;
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        LocalDateTime date = LocalDateTime.now();
        StationID stationID = new StationID("ST_001", location);


        assertThrows(InvalidPairingArgsException.class, () -> simulatedServer.registerPairing(user, vehicleID, stationID, location, date));
    }

    @Test
    void testRegisterPairing_VehicleNotAvailable()  {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH999999"); // Vehicle no disponible
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        LocalDateTime date = LocalDateTime.now();
        StationID stationID = new StationID("ST_001", location);


        assertThrows(InvalidPairingArgsException.class, () -> simulatedServer.registerPairing(user, vehicleID, stationID, location, date));
    }



    @Test
    void testRegisterPairing_InvalidLocation() {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");

        // Ubicació registrada a l'estació
        GeographicPoint registeredLocation = new GeographicPoint(41.3851f, 2.1734f);

        // Ubicació incorrecta proporcionada al test
        GeographicPoint invalidLocation = new GeographicPoint(0f, 0f);

        // Crear l'estació amb la ubicació registrada
        StationID stationID = new StationID("ST_001", registeredLocation);

        // Prova que es llanci l'excepció perquè la ubicació proporcionada no coincideix amb la registrada
        assertThrows(InvalidPairingArgsException.class,
                () -> simulatedServer.registerPairing(user, vehicleID, stationID, invalidLocation, LocalDateTime.now()),
                "Hauria de llençar una excepció perquè la ubicació proporcionada no és vàlida.");
    }


    @Test
    void testRegisterPairing_VehicleMarkedUnavailable() throws ConnectException, InvalidPairingArgsException {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        LocalDateTime date = LocalDateTime.now();
        StationID stationID = new StationID("ST_001", location);


        simulatedServer.registerPairing(user, vehicleID, stationID, location, date);

        // Verifiquem que el vehicle no està disponible
        assertThrows(PMVNotAvailException.class, () -> simulatedServer.checkPMVAvail(vehicleID));
    }
    @Test
    void testRegisterPairing_VehicleNotAtStation() {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");

        // Ubicació registrada per a ST_001
        GeographicPoint locationForStation1 = new GeographicPoint(41.3851f, 2.1734f);

        // Ubicació proporcionada per a ST_002 (diferent)
        GeographicPoint locationForStation2 = new GeographicPoint(41.3879f, 2.1699f);

        // Crear estacions amb les seves ubicacions associades
        StationID station1 = new StationID("ST_001", locationForStation1);
        StationID station2 = new StationID("ST_002", locationForStation2);

        // Intentar emparellar un vehicle a una estació amb una ubicació diferent
        assertThrows(InvalidPairingArgsException.class,
                () -> simulatedServer.registerPairing(user, vehicleID, station2, locationForStation1, LocalDateTime.now()),
                "Hauria de llençar una excepció perquè la ubicació no coincideix amb l'estació indicada.");
    }



    @Test
    void testStopPairing_Success() throws ConnectException, InvalidPairingArgsException {
        // Configuració inicial
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        LocalDateTime date = LocalDateTime.now();
        StationID stationID = new StationID("ST_001", location);


        // Registra el viatge
        simulatedServer.registerPairing(user, vehicleID, stationID, location, date);

        // Paràmetres per aturar el viatge
        float avgSpeed = 15.0f;
        float distance = 2.0f;
        int duration = 10;
        BigDecimal fare = new BigDecimal("2.50");

        // Atura el viatge
        assertDoesNotThrow(() -> simulatedServer.stopPairing(user, vehicleID, stationID, location, date, avgSpeed, distance, duration, fare));
    }


    @Test
    void testStopPairing_InvalidArgs() {
        UserAccount user = null;
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        LocalDateTime date = LocalDateTime.now();
        StationID stationID = new StationID("ST_001", location);

        float avgSpeed = 15.0f;
        float distance = 2.0f;
        int duration = 10;
        BigDecimal fare = new BigDecimal("2.50");

        assertThrows(InvalidPairingArgsException.class, () -> simulatedServer.stopPairing(user, vehicleID, stationID, location, date, avgSpeed, distance, duration, fare));
    }
    @Test
    void testStopPairing_VehicleAvailableAfterStop() throws ConnectException, InvalidPairingArgsException {
        // Registra el vehicle amb un viatge actiu
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");

        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_001", location);

        LocalDateTime date = LocalDateTime.now();

        // Registra el servei abans d'aturar-lo
        simulatedServer.registerPairing(user, vehicleID, stationID, location, date);

        float avgSpeed = 15.0f;
        float distance = 2.0f;
        int duration = 10;
        BigDecimal fare = new BigDecimal("2.50");

        // Atura el viatge
        simulatedServer.stopPairing(user, vehicleID, stationID, location, date, avgSpeed, distance, duration, fare);

        // Comprova que el vehicle està disponible després del desemparellament
        assertDoesNotThrow(() -> simulatedServer.checkPMVAvail(vehicleID));
    }


    @Test
    void testStopPairing_VehicleStationUpdated() throws ConnectException, InvalidPairingArgsException {
        // Configuració inicial
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_001", location);

        LocalDateTime date = LocalDateTime.now();

        // Registra el viatge
        simulatedServer.registerPairing(user, vehicleID, stationID, location, date);

        // Paràmetres per aturar el viatge
        float avgSpeed = 15.0f;
        float distance = 2.0f;
        int duration = 10;
        BigDecimal fare = new BigDecimal("2.50");

        // Atura el viatge
        simulatedServer.stopPairing(user, vehicleID, stationID, location, date, avgSpeed, distance, duration, fare);

        // Comprova que l'estació registrada pel vehicle és la correcta
        assertEquals(stationID, simulatedServer.getLocation(vehicleID));
    }






    @Test
    void testUnpairRegisterService_Success() throws InvalidPairingArgsException, ConnectException, PairingNotFoundException {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        LocalDateTime date = LocalDateTime.now();
        StationID stationID = new StationID("ST_001", location);


        // Registra el servei
        simulatedServer.registerPairing(user, vehicleID, stationID, location, date);

        System.out.println("Simulated Database després registerPairing: " + simulatedServer.getSimulatedDatabase());

        // Recuperar el servei associat
        JourneyService journeyService = simulatedServer.getSimulatedDatabase().get(vehicleID);
        assertNotNull(journeyService, "El servei no s'ha registrat correctament.");
        assertTrue(journeyService.isActive(), "El servei no està actiu després de registrar-lo.");

        System.out.println("JourneyService VehicleID: " + journeyService.getVehicle());
        System.out.println("Test VehicleID: " + vehicleID);

        // Desemparellar el servei
        assertDoesNotThrow(() -> simulatedServer.unPairRegisterService(journeyService));

        // Comprova que el servei ha estat eliminat
        assertFalse(simulatedServer.getSimulatedDatabase().containsKey(vehicleID), "El servei encara està registrat al servidor.");
        assertTrue(simulatedServer.getAvailableVehicles().contains(vehicleID), "El vehicle no s'ha marcat com disponible.");
    }






    @Test
    void testUnpairRegisterService_PairingNotFound() {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_001", location);

        // Crear un servei no registrat
        ServiceID serviceID=new ServiceID( "JNY_" + vehicleID.getId() + "_123456"); // Generem un ID fals
        JourneyService journeyService = new JourneyService(serviceID, user, vehicleID, stationID);
        journeyService.setServiceInit(); // Iniciar el servei

        // Intentar desemparellar el servei no existent
        assertThrows(PairingNotFoundException.class, () -> simulatedServer.unPairRegisterService(journeyService));
    }


    @Test
    void testUnpairRegisterService_NullService() {
        // Intentar desemparellar un servei nul
        assertThrows(PairingNotFoundException.class, () -> simulatedServer.unPairRegisterService(null));
    }

    @Test
    void testUnpairRegisterService_ServiceNotInitialized() {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_001", location);
        // Crear un servei sense inicialitzar
        ServiceID serviceID=new ServiceID( "JNY_" + vehicleID.getId() + "_123456"); // Generem un ID fals
        JourneyService journeyService = new JourneyService(serviceID, user, vehicleID, stationID);
        // NO inicialitzem el servei amb setServiceInit()

        // Intentar desemparellar el servei no inicialitzat
        assertThrows(PairingNotFoundException.class, () -> simulatedServer.unPairRegisterService(journeyService));
    }


    @Test
    void testUnpairRegisterService_VehicleNotRegistered() throws InvalidPairingArgsException, ConnectException {
        UserAccount user = new UserAccount("User123");
        VehicleID registeredVehicle = new VehicleID("VH654321");
        VehicleID unregisteredVehicle = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_001", location);
        LocalDateTime date = LocalDateTime.now();

        // Registrar l'aparellament per un altre vehicle
        simulatedServer.registerPairing(user, registeredVehicle, stationID, location, date);

        // Crear el servei amb el vehicle no registrat
        ServiceID serviceID=new ServiceID( "JNY_" + unregisteredVehicle.getId() + "_123456"); // Generem un ID fals
        JourneyService journeyService = new JourneyService(serviceID, user, unregisteredVehicle, stationID);

        journeyService.setServiceInit(); // Iniciar el servei

        // Intentar desemparellar
        assertThrows(PairingNotFoundException.class, () -> simulatedServer.unPairRegisterService(journeyService));
    }


    @Test
    void testSetPairing_Success() {
        UserAccount user = new UserAccount("User123");
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_001", location);
        LocalDateTime date = LocalDateTime.now();

        // Comprovar que no llença cap excepció
        assertDoesNotThrow(() -> simulatedServer.setPairing(user, vehicleID, stationID, location, date));

        // Recuperar el servei associat
        JourneyService journey = simulatedServer.getSimulatedDatabase().get(vehicleID);

        // Verificar que les dades s'han guardat correctament
        assertNotNull(journey, "El servei no s'ha registrat correctament al servidor.");

        // Generar el ServiceID esperat de la mateixa manera que a `setPairing`
        String expectedServiceID = "SRV_" + vehicleID.getId() + "_" + date.toLocalTime().toSecondOfDay();

        assertEquals(expectedServiceID, journey.getServiceID().getId(), "El ServiceID no coincideix.");
        assertEquals(vehicleID, journey.getVehicle(), "El VehicleID no coincideix.");
        assertEquals(user, journey.getUser(), "L'usuari associat no coincideix.");
        assertTrue(journey.isActive(), "El servei no està actiu.");

        // Verificar que el vehicle no està disponible
        assertFalse(simulatedServer.getAvailableVehicles().contains(vehicleID), "El vehicle encara està disponible després de l'emparellament.");
    }




    @Test
    void testRegisterLocation_Success() {
        VehicleID vehicleID = new VehicleID("VH123456");
        GeographicPoint location = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_001", location);
        // Comprovar que el mètode registerLocation no llença cap excepció
        assertDoesNotThrow(() -> simulatedServer.registerLocation(vehicleID, stationID));

        // Comprovar que la ubicació registrada es pot recuperar correctament
        assertEquals(stationID, simulatedServer.getLocation(vehicleID));
    }


    @Test
    void testRegisterLocation_NullArguments() {
        VehicleID vehicleID = null;
        StationID stationID = null;

        // Comprovar que l'excepció IllegalArgumentException es llença quan tant el VehicleID com l'StationID són null
        assertThrows(IllegalArgumentException.class, () -> simulatedServer.registerLocation(vehicleID, stationID));
    }

    @Test
    void testGetLocation_VehicleNotRegistered() {
        VehicleID vehicleID = new VehicleID("VH123456");

        // Comprovar que es retorna null si el vehicle no ha estat registrat
        assertNull(simulatedServer.getLocation(vehicleID));
    }

    @Test
    void testRegisterPaymentSuccess() {
        ServiceID serviceID = new ServiceID("SRV123");
        UserAccount user = new UserAccount("user123");
        BigDecimal amount = new BigDecimal("25.50");
        char paymentMethod = 'W'; // Wallet

        // Comprova que no es llença cap excepció amb valors vàlids
        assertDoesNotThrow(() -> simulatedServer.registerPayment(serviceID, user, amount, paymentMethod),
                "El registre del pagament hauria de completar-se sense errors.");
    }

    @Test
    void testRegisterPaymentInvalidPaymentMethod() {
        ServiceID serviceID = new ServiceID("SRV123");
        UserAccount user = new UserAccount("user123");
        BigDecimal amount = new BigDecimal("25.50");
        char invalidPaymentMethod = 'X'; // Mètode de pagament no vàlid

        // Comprova que es llença una excepció per mètode de pagament invàlid
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> simulatedServer.registerPayment(serviceID, user, amount, invalidPaymentMethod),
                "S'espera una excepció per mètode de pagament invàlid.");

        assertEquals("Mètode de pagament invàlid. S'esperen 'W' (wallet), 'P' (passarel·la), 'T' (targeta) o 'B' (Bizum).",
                exception.getMessage());
    }

    @Test
    void testRegisterPaymentNullArguments() {
        ServiceID serviceID = null;
        UserAccount user = null;
        BigDecimal amount = null;
        char paymentMethod = 'W';

        // Comprova que es llença una excepció per valors nuls
        assertThrows(IllegalArgumentException.class,
                () -> simulatedServer.registerPayment(serviceID, user, amount, paymentMethod),
                "S'espera una excepció quan els paràmetres són nuls.");
    }

    @Test
    void testRegisterPaymentConnectionError() {
        ServiceID errorServiceID = new ServiceID("ERROR_CON"); // Simula un ID que provoca error de connexió
        UserAccount user = new UserAccount("user123");
        BigDecimal amount = new BigDecimal("25.50");
        char paymentMethod = 'W';

        // Comprova que es llença una excepció de connexió
        ConnectException exception = assertThrows(ConnectException.class,
                () -> simulatedServer.registerPayment(errorServiceID, user, amount, paymentMethod),
                "S'espera una excepció de connexió amb el servidor.");

        assertEquals("Error de connexió amb el servidor.", exception.getMessage());
    }







}
