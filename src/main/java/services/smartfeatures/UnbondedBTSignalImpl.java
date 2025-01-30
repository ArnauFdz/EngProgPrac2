package services.smartfeatures;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import micromobility.*; // Exemples de dependències necessàries


/**
 * Implementació de la interfície UnbondedBTSignal que emet l'ID de l'estació de manera continuada.
 */
public class UnbondedBTSignalImpl implements UnbondedBTSignal {

    private final String stationID; // ID de l'estació a transmetre
    private final int broadcastInterval; // Interval en segons entre emissions
    private JourneyRealizeHandler handler;

    public UnbondedBTSignalImpl(String stationID, int broadcastInterval, JourneyRealizeHandler handler) {
        this.stationID = stationID;
        this.broadcastInterval = broadcastInterval;
        this.handler = handler;
    }

    @Override
    public void BTbroadcast() throws ConnectException {
        try {

            while (true) {
                handler.broadcastStationID(stationID); //Error que es solucionara a posteriori en els canvis de JourneyRealizeHandler
                TimeUnit.SECONDS.sleep(broadcastInterval); // Pausa entre emissions
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurar estat d'interrupció
            throw new ConnectException("Emissió interrompuda.");
        } catch (Exception e) {
            throw new ConnectException("Error inesperat en la connexió Bluetooth.");
        }
    }
}
