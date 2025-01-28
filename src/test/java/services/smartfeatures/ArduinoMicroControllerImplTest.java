package services.smartfeatures;

import java.net.ConnectException;
import exceptions.PMVPhisicalException;
import exceptions.ProceduralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArduinoMicroControllerImplTest {

    private ArduinoMicroControllerImpl arduinoMicroController;

    @BeforeEach
    public void setUp() {
        arduinoMicroController = new ArduinoMicroControllerImpl();
    }

    @Test
    public void testSetBTconnection_Success() throws ConnectException {
        // Comprovem que la connexió es realitza correctament la primera vegada
        assertDoesNotThrow(() -> arduinoMicroController.setBTconnection());

        // Comprovem que la connexió llança excepció si es torna a intentar
        assertThrows(ConnectException.class, () -> arduinoMicroController.setBTconnection());
    }

    @Test
    public void testSetBTconnection_AlreadyConnected() {
        assertDoesNotThrow(() -> arduinoMicroController.setBTconnection());
        // Provar si es llança l'error quan es connecta de nou
        assertThrows(ConnectException.class, () -> arduinoMicroController.setBTconnection());
    }

    @Test
    public void testStartDriving_Success() throws ConnectException, PMVPhisicalException, ProceduralException {
        arduinoMicroController.setBTconnection(); // Establim la connexió BT

        // Simulació d'inici de desplaçament
        arduinoMicroController.startDriving();

        // Comprovem que no es llança cap excepció en cridar startDriving() una altra vegada
        assertThrows(ProceduralException.class, () -> arduinoMicroController.startDriving());
    }




    @Test
    public void testStartDriving_NotConnected() {
        assertThrows(ConnectException.class, () -> arduinoMicroController.startDriving());
    }

    @Test
    public void testStartDriving_AlreadyDriving() throws ConnectException, PMVPhisicalException, ProceduralException {
        arduinoMicroController.setBTconnection();
        arduinoMicroController.startDriving();
        // Provar si es llança l'error quan el vehicle ja està en moviment
        assertThrows(ProceduralException.class, () -> arduinoMicroController.startDriving());
    }

    @Test
    public void testStopDriving_Success() throws ConnectException, PMVPhisicalException, ProceduralException {
        arduinoMicroController.setBTconnection();
        arduinoMicroController.startDriving();
        arduinoMicroController.stopDriving();
        // Comprovem que el vehicle ha estat aturat
        assertThrows(ProceduralException.class, () -> arduinoMicroController.stopDriving());
    }

    @Test
    public void testStopDriving_NotDriving() throws ConnectException {
        arduinoMicroController.setBTconnection();
        // Provar si es llança l'error quan el vehicle no està en moviment
        assertThrows(ProceduralException.class, () -> arduinoMicroController.stopDriving());
    }

    @Test
    public void testStopDriving_NotConnected() {
        assertThrows(ConnectException.class, () -> arduinoMicroController.stopDriving());
    }

    @Test
    public void testUndoBTconnection_Success() throws ConnectException {
        arduinoMicroController.setBTconnection();
        arduinoMicroController.undoBTconnection();
        // Comprovem que la connexió Bluetooth s'ha desconnectat correctament
        assertDoesNotThrow(() -> arduinoMicroController.undoBTconnection());
    }

    @Test
    public void testUndoBTconnection_NotConnected() {
        // Provar si es llança l'error quan no hi ha connexió Bluetooth establerta
        assertDoesNotThrow(() -> arduinoMicroController.undoBTconnection());
    }
}
