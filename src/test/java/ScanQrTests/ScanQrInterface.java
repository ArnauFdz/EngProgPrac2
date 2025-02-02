package ScanQrTests;

import data.StationID;
import micromobility.JourneyRealizeHandler;
import org.junit.jupiter.api.BeforeEach;
import services.Server;
import services.ServerImp;
import services.smartfeatures.*;

public interface ScanQrInterface {
    default JourneyRealizeHandler getHandler(){
        StationID stationID = new StationID("STN_123");
        Server server = new ServerImp();
        QRDecoder qrDecoder = new QRDecoderImpl();
        ArduinoMicroController arduinoMicroController = new ArduinoMicroControllerImpl();
        UnbondedBTSignal unbondedBTSignal = new UnbondedBTSignalImpl(stationID,1,new JourneyRealizeHandler());


        JourneyRealizeHandler journeyRealizeHandler = new JourneyRealizeHandler(server,qrDecoder,arduinoMicroController,unbondedBTSignal);
        return journeyRealizeHandler;
    }

    @BeforeEach
    void setUp() throws Exception;

    void serviceScenarioSetup() throws Exception;

    void ThrowsCorruptedImageExceptionTest() throws Exception;
}
