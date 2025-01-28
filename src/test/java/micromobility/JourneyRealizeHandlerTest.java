package micromobility;
import java.util.Set;
import data.*;
import exceptions.*;
import micromobility.payment.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ServerImp;
import services.smartfeatures.ArduinoMicroControllerImpl;
import services.smartfeatures.QRDecoderImpl;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JourneyRealizeHandlerTest {

    private ServerImp server;
    private QRDecoderImpl qrDecoder;
    private ArduinoMicroControllerImpl microController;
    private Wallet wallet;
    private JourneyService journey;
    private JourneyRealizeHandler handler;
    private BigDecimal costPerKm;
    private BigDecimal costPerMinute;
    private double[] calculatedValues;

    @BeforeEach
    void setUp() {
        // Configurar els components necessaris
        server = new ServerImp();
        qrDecoder = new QRDecoderImpl();
        microController = new ArduinoMicroControllerImpl();
        wallet = new Wallet(new BigDecimal("50.00"));

        GeographicPoint ubicacio = new GeographicPoint(41.3851f, 2.1734f);
        StationID startStation = new StationID("ST_001", ubicacio);
        UserAccount user = new UserAccount("User123");
        VehicleID vehicle = new VehicleID("VH1234");

        // Crear JourneyService i inicialitzar-lo
        journey = new JourneyService(new ServiceID("SV1234"), user, vehicle, startStation);
        journey.setServiceInit();

        // Registrar el JourneyService al servidor
        server.getSimulatedDatabase().put(vehicle, journey);

        // Crear el handler amb el JourneyService
        handler = new JourneyRealizeHandler(server, qrDecoder, microController, wallet, journey);

        // Definir costos personalitzats
        costPerKm = new BigDecimal("0.5");
        costPerMinute = new BigDecimal("0.2");

        // Resultats simulats de calculateValues
        calculatedValues = new double[]{5.0, 15, 20.0}; // distància, duració, velocitat mitjana
        server.setAvailableVehicles(Set.of(
                new VehicleID("VH123456"), // Vehicle retornat pel QRDecoderImpl
                new VehicleID("VH654321")
        ));

    }

    @Test
    void testScanQR_Success() throws Exception {
        BufferedImage validQR = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        qrDecoder.getVehicleID(validQR); // Simulació del QR vàlid
        assertDoesNotThrow(() -> handler.scanQR(validQR));
    }

    @Test
    void testScanQR_NullImage() {
        BufferedImage invalidQR = null;
        assertThrows(ProceduralException.class, () -> handler.scanQR(invalidQR));
    }

    @Test
    void testUnpairVehicle_Success() throws Exception {
        // Obtenir un vehicle que estigui disponible
        VehicleID availableVehicle = server.getAvailableVehicles().iterator().next();

        // Assignar el vehicle disponible al JourneyService
        journey = new JourneyService(new ServiceID("SRV_TEST"), journey.getUser(), availableVehicle, journey.getStartStation());
        journey.setServiceInit();

        // Utilitzar valors correctes per registrar el pairing
        GeographicPoint ubicacio = new GeographicPoint(41.3851f, 2.1734f);
        StationID startStation = new StationID("ST_001", ubicacio);
        LocalDateTime now = LocalDateTime.now();

        // Registra el JourneyService al servidor
        server.registerPairing(journey.getUser(), availableVehicle, startStation, ubicacio, now);

        // Ara prova de desemparellar-lo
        assertDoesNotThrow(() -> handler.unPairVehicle(journey),
                "No hauria de llençar cap excepció en desemparellar el vehicle.");
    }

    @Test
    void testUnpairVehicle_NotActive() {
        journey.setServiceFinish(); // Marcar el servei com no actiu
        assertThrows(ProceduralException.class, () -> handler.unPairVehicle(journey));
    }

    @Test
    void testBroadcastStationID_Success() {
        GeographicPoint ubicacio = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_002", ubicacio);
        assertDoesNotThrow(() -> handler.broadcastStationID(stationID));
    }

    @Test
    void testBroadcastStationID_NullID() {
        StationID stationID = null;
        assertThrows(IllegalArgumentException.class, () -> handler.broadcastStationID(stationID));
    }

    @Test
    void testStartDriving_Success() throws Exception {
        microController.setBTconnection(); // Connexió d bluetooth abans d'iniciar la conducció
        assertDoesNotThrow(() -> handler.startDriving());
        assertTrue(handler.isDriving());
    }

    @Test
    void testStopDriving_Success() throws Exception {
        microController.setBTconnection();
        handler.startDriving();
        assertDoesNotThrow(() -> handler.stopDriving());
        assertFalse(handler.isDriving());
    }

    @Test
    void testCalculateValuesAndImportIntegration() throws Exception {
        BigDecimal importCalculated = handler.calculateImport(
                (float) calculatedValues[0], (int) calculatedValues[1], (float) calculatedValues[2], costPerKm, costPerMinute);
        assertNotNull(importCalculated);
        assertTrue(importCalculated.compareTo(BigDecimal.ZERO) > 0);
    }


    @Test
    void testCalculateImportInvalidParameters() {
        // Comprova que calculateImport llença excepció amb paràmetres no vàlids
        assertThrows(IllegalArgumentException.class,
                () -> handler.calculateImport(-5.0f, 15, 20.0f, costPerKm, costPerMinute),
                "Distància negativa hauria de llençar excepció.");
        assertThrows(IllegalArgumentException.class,
                () -> handler.calculateImport(5.0f, -15, 20.0f, costPerKm, costPerMinute),
                "Duració negativa hauria de llençar excepció.");
        assertThrows(IllegalArgumentException.class,
                () -> handler.calculateImport(5.0f, 15, -20.0f, costPerKm, costPerMinute),
                "Velocitat mitjana negativa hauria de llençar excepció.");
    }


    @Test
    void testSelectPaymentMethodSuccess() throws Exception {
        char method = 'W'; // Wallet

        // Comprova que no es llença cap excepció per un mètode de pagament vàlid
        assertDoesNotThrow(() -> handler.selectPaymentMethod(method));

        // Comprova el missatge de selecció correcte (opcional, segons si captures la sortida)
        System.out.println("Mètode de pagament seleccionat correctament: Wallet.");
    }

    @Test
    void testSelectPaymentMethodInvalidOption() {
        char invalidMethod = 'X'; // Opció no vàlida

        // Comprova que es llença una excepció amb un mètode de pagament invàlid
        ProceduralException exception = assertThrows(ProceduralException.class,
                () -> handler.selectPaymentMethod(invalidMethod));

        assertEquals("Mètode de pagament no vàlid.", exception.getMessage());
    }

    @Test
    void testSelectPaymentMethodCreditCard() throws Exception {
        char method = 'C'; // Crèdit

        // Comprova que no es llença cap excepció per un mètode de pagament vàlid
        assertDoesNotThrow(() -> handler.selectPaymentMethod(method));

        // Comprova el missatge de selecció correcte (opcional)
        System.out.println("Mètode de pagament seleccionat correctament: Crèdit.");
    }

    @Test
    void testSelectPaymentMethodDebitCard() throws Exception {
        char method = 'D'; // Dèbit

        // Comprova que no es llença cap excepció per un mètode de pagament vàlid
        assertDoesNotThrow(() -> handler.selectPaymentMethod(method));

        // Comprova el missatge de selecció correcte (opcional)
        System.out.println("Mètode de pagament seleccionat correctament: Dèbit.");
    }

    @Test
    void testSelectPaymentMethodPayPal() throws Exception {
        char method = 'P'; // PayPal

        // Comprova que no es llença cap excepció per un mètode de pagament vàlid
        assertDoesNotThrow(() -> handler.selectPaymentMethod(method));

        // Comprova el missatge de selecció correcte (opcional)
        System.out.println("Mètode de pagament seleccionat correctament: PayPal.");
    }


    @Test
    void testBroadcastStationIDDuplicate() throws Exception {
        // Comprova si es gestiona correctament un broadcast duplicat d'una estació
        GeographicPoint ubicacio = new GeographicPoint(41.3851f, 2.1734f);
        StationID stationID = new StationID("ST_001", ubicacio);
        handler.broadcastStationID(stationID);

        // Prova d'enviar el mateix broadcast dues vegades
        assertDoesNotThrow(() -> handler.broadcastStationID(stationID),
                "Broadcast duplicat no hauria de llençar excepció.");
    }

    @Test
    void testStopDrivingWithoutStart() {
        // Comprova que no es pot parar la conducció sense haver-la començat
        ProceduralException exception = assertThrows(ProceduralException.class, () -> handler.stopDriving());
        assertEquals("No es pot aturar el vehicle perquè no està en conducció.", exception.getMessage());
    }

    @Test
    void testCalculateValuesAndImport() {
        // Defineix la ubicació final i el temps d'acabament
        GeographicPoint endLocation = new GeographicPoint(41.3964f, 2.1904f);
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(15);

        // Defineix els costos per cridar a calculateimport
        BigDecimal costPerKm = new BigDecimal("0.5");
        BigDecimal costPerMinute = new BigDecimal("0.2");

        // Crida a calculateValues per obtenir els valors
        double[] calculatedValues = handler.calculateValues(endLocation, endTime);

        // Comprova que els valors calculats són correctes
        assertNotNull(calculatedValues, "Els valors calculats no haurien de ser nuls.");
        assertEquals(3, calculatedValues.length, "S'han de retornar exactament 3 valors.");
        assertTrue(calculatedValues[0] > 0, "La distància ha de ser positiva.");
        assertTrue(calculatedValues[1] > 0, "La duració ha de ser positiva.");
        assertTrue(calculatedValues[2] > 0, "La velocitat mitjana ha de ser positiva.");

        // Converteix els valors calculats per passar-los a calculateImport
        float distance = (float) calculatedValues[0];
        int duration = (int) calculatedValues[1];
        float avgSpeed = (float) calculatedValues[2];

        // Crida a calculateImport per calcular l'import
        BigDecimal calculatedImport = handler.calculateImport(distance, duration, avgSpeed, costPerKm, costPerMinute);

        // Comprova que l'import calculat és correcte
        assertNotNull(calculatedImport, "L'import calculat no hauria de ser nul.");
        assertTrue(calculatedImport.compareTo(BigDecimal.ZERO) > 0, "L'import calculat ha de ser positiu.");

        // Calcula l'import esperat manualment per verificar-lo
        BigDecimal expectedImport = BigDecimal.valueOf(distance).multiply(costPerKm)
                .add(BigDecimal.valueOf(duration).multiply(costPerMinute));
        assertEquals(0, calculatedImport.compareTo(expectedImport), "L'import calculat no coincideix amb l'esperat.");
    }

    @Test
    void testRealizePaymentSuccess() throws Exception {
        // Configura el saldo inicial i l'import a pagar
        BigDecimal initialBalance = wallet.getBalance();
        BigDecimal amountToPay = new BigDecimal("10.00");

        // Comprova que no es llença cap excepció per un pagament vàlid
        assertDoesNotThrow(() -> handler.realizePayment(amountToPay));

        // Comprova que el saldo s'ha reduït correctament
        BigDecimal expectedBalance = initialBalance.subtract(amountToPay);
        assertEquals(expectedBalance, wallet.getBalance(), "El saldo del moneder no és correcte després del pagament.");
    }

    @Test
    void testRealizePaymentInsufficientFunds() throws Exception {
        // Configura un import a pagar superior al saldo del moneder
        BigDecimal amountToPay = wallet.getBalance().add(new BigDecimal("10.00"));

        // Comprova que es llença una excepció de fons insuficients
        NotEnoughWalletException exception = assertThrows(NotEnoughWalletException.class,
                () -> handler.realizePayment(amountToPay)
        );

        // Comprova el missatge de l'excepció
        assertEquals("Saldo insuficient al moneder.", exception.getMessage());
    }






}
