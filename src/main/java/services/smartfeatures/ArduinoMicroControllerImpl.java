package services.smartfeatures;

import java.net.ConnectException;
import exceptions.PMVPhisicalException;
import exceptions.ProceduralException;
import micromobility.JourneyRealizeHandler;
import micromobility.JourneyService;

public class ArduinoMicroControllerImpl implements ArduinoMicroController {

    private boolean btConnected = false; // Estat de la connexió Bluetooth
    private boolean isDriving = false; // Estat de la conducció del vehicle
    private JourneyRealizeHandler handler;

    @Override
    public void setBTconnection(JourneyRealizeHandler handler) throws ConnectException {
        if (btConnected) {
            throw new ConnectException("La connexió Bluetooth ja està establerta.");
        }
        btConnected = true;
        this.handler = handler;
        System.out.println("Connexió Bluetooth establerta.");
    }

    public void setHandler(JourneyRealizeHandler handler){
        this.handler = handler;
    }

    @Override
    public void startDriving() throws  ConnectException, ProceduralException {
        if (!btConnected) {
            throw new ConnectException("No s'ha establert la connexió Bluetooth.");
        }

        if (isDriving) {
            throw new ProceduralException("El vehicle ja està en moviment.");
        }

        handler.startDriving();
        System.out.println("Vehicle en moviment: el conductor ha començat a conduir.");
    }

    @Override
    public void stopDriving() throws PMVPhisicalException, ConnectException, ProceduralException {
        if (!btConnected) {
            throw new ConnectException("No s'ha establert la connexió Bluetooth.");
        }

        if (!isDriving) {
            throw new ProceduralException("El vehicle no està en moviment.");
        }

        handler.stopDriving();
        System.out.println("Vehicle aturat: el conductor ha detingut el vehicle.");
    }

    @Override
    public void undoBTconnection() {
        if (!btConnected) {
            System.out.println("La connexió Bluetooth no està establerta.");
            return;
        }
        btConnected = false;
        System.out.println("Connexió Bluetooth desconnectada.");
    }
}
