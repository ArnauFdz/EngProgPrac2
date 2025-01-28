package services.smartfeatures;

import exceptions.PMVPhisicalException;
import exceptions.ProceduralException;

import java.net.ConnectException;

/**
 * Interfície que defineix les operacions per interactuar amb el microcontrolador Arduino d'un vehicle.
 * S'utilitza per gestionar l'estat de conducció del vehicle.
 *
 * Relació:
 * - Utilitzada en el dummy `DummyArduinoMicroController` per simular comportaments en proves.
 * - Provada a `ArduinoMicroControllerTest` per comprovar el comportament en escenaris operatius i d'errors.
 */
public interface ArduinoMicroController { // Software for microcontrollers
    public void setBTconnection () throws ConnectException, ConnectException;
    public void startDriving () throws PMVPhisicalException, ConnectException,
            ProceduralException;
    public void stopDriving () throws PMVPhisicalException, ConnectException,
            ProceduralException;
    public void undoBTconnection ();
}

