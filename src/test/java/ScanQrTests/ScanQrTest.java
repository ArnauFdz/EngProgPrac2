package ScanQrTests;

import data.VehicleID;
import exceptions.ProceduralException;
import micromobility.JourneyRealizeHandler;
import micromobility.JourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.Server;
import services.ServerImp;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.ArduinoMicroControllerImpl;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.QRDecoderImpl;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class ScanQrTest implements ScanQrInterface {


    private JourneyRealizeHandler handler;
    private ArduinoMicroController arduino;
    private Server server;
    private QRDecoder qrDecoder;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        this.handler = getHandler();
        serviceScenarioSetup();
    }


    @Override
    public void serviceScenarioSetup() throws Exception {
        this.arduino = new ArduinoMicroControllerImpl();
        this.server = new ServerImp();
        this.qrDecoder = new QRDecoderImpl();
        this.handler.setServer(server);
        this.handler.setQrDecoder(qrDecoder);
        this.handler.setMicroController(arduino);
    }

    @Override
    @Test
    public void ThrowsCorruptedImageExceptionTest() throws Exception {
        assertThrows(ProceduralException.class, () -> handler.scanQR(null));
    }
}