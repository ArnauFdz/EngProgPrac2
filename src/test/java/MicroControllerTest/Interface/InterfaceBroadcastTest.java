package MicroControllerTest.Interface;

import data.StationID;
import micromobility.JourneyRealizeHandler;
import services.Server;
import services.ServerImp;
import services.smartfeatures.*;

import java.net.ConnectException;

public interface InterfaceBroadcastTest {
    default JourneyRealizeHandler getHandler(){
        StationID stationID = new StationID("STN_123");
        Server server = new ServerImp();
        QRDecoder qrDecoder = new QRDecoderImpl();
        ArduinoMicroController arduinoMicroController = new ArduinoMicroControllerImpl();
        UnbondedBTSignal unbondedBTSignal = new UnbondedBTSignalImpl(stationID,1,new JourneyRealizeHandler());


        JourneyRealizeHandler journeyRealizeHandler = new JourneyRealizeHandler(server,qrDecoder,arduinoMicroController,unbondedBTSignal);
        return journeyRealizeHandler;
    }

    void testConectionException() throws ConnectException;
}
