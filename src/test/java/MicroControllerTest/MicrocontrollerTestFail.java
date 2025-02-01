package MicroControllerTest;

import data.StationID;
import micromobility.JourneyRealizeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.UnbondedBTSignal;
import services.smartfeatures.UnbondedBTSignalImpl;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.net.ConnectException;

public class MicrocontrollerTestFail implements InterfaceBroadcastTest {
    private JourneyRealizeHandler journeyRealizeHandler;
    private UnbondedBTSignal unbondedBTSignal;

    @BeforeEach
    public void setUp(){
        journeyRealizeHandler = getHandler();
        setUpScenario();
    }

    public void setUpScenario(){
        unbondedBTSignal = new UnbondedBTSignalImpl(new StationID("STN_123"),1, journeyRealizeHandler);
        unbondedBTSignal.setOn(false);
    }

    @Test
    public void testConectionException() throws ConnectException{
        assertThrows(ConnectException.class, ()-> unbondedBTSignal.BTbroadcast());
    }

}
