package MicroControllerTest;

import MicroControllerTest.Interface.InterfaceBroadcastTest;
import micromobility.JourneyRealizeHandler;
import services.smartfeatures.UnbondedBTSignal;
import data.StationID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.UnbondedBTSignalImpl;

import java.net.ConnectException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class microControllerTestExit implements InterfaceBroadcastTest {
    private JourneyRealizeHandler journeyRealizeHandler;
    private UnbondedBTSignal unbondedBTSignal;

    @Override
    public JourneyRealizeHandler getHandler() {
        return InterfaceBroadcastTest.super.getHandler();
    }

    @BeforeEach
    public void setUp(){
        journeyRealizeHandler = getHandler();
        setUpScenario();
    }

    public void setUpScenario(){
        unbondedBTSignal = new UnbondedBTSignalImpl(new StationID("STN_123"),1, journeyRealizeHandler);
    }

    @Test
    @Override
    public void testConectionException() throws ConnectException{
        unbondedBTSignal.BTbroadcast();
        assertEquals(new StationID("STN_123"), journeyRealizeHandler.getSt());
    }


/*
    @BeforeEach
    public void setUp(){
        journeyRealizeHandler = getHandler();
        setUpScenario();
    }

    public void setUpScenario(){
        unbondedBTSignal = new UnbondedBTSignalImpl(new StationID("STN_123"),1, journeyRealizeHandler);
    }

    @Override
    @Test
    public void testConectionException() throws ConnectException {
        unbondedBTSignal.BTbroadcast();
        assertEquals(new StationID("STN_123"), journeyRealizeHandler.getSt());
    }

 */
}
