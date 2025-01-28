package services.smartfeatures;

import java.net.ConnectException;
import exceptions.PMVPhisicalException;
import exceptions.ProceduralException;

public class ArduinoMicroControllerImpl implements ArduinoMicroController {

    private boolean btConnected = false; // Estat de la connexió Bluetooth
    private boolean isDriving = false; // Estat de la conducció del vehicle

    @Override
    public void setBTconnection() throws ConnectException {
        if (btConnected) {
            throw new ConnectException("La connexió Bluetooth ja està establerta.");
        }
        btConnected = true;
        System.out.println("Connexió Bluetooth establerta.");
    }

    @Override
    public void startDriving() throws  ConnectException, ProceduralException {
        if (!btConnected) {
            throw new ConnectException("No s'ha establert la connexió Bluetooth.");
        }

        if (isDriving) {
            throw new ProceduralException("El vehicle ja està en moviment.");
        }

        isDriving = true;
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

        isDriving = false;
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
