package services.smartfeatures;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import data.StationID;
import micromobility.*; // Exemples de dependències necessàries


/**
 * Implementació de la interfície UnbondedBTSignal que emet l'ID de l'estació de manera continuada.
 */
public class UnbondedBTSignalImpl implements UnbondedBTSignal {

    private StationID stationID; // ID de l'estació a transmetre
    private final int broadcastInterval; // Interval en segons entre emissions
    private JourneyRealizeHandler handler;
    private Boolean isOn;

    public UnbondedBTSignalImpl(StationID stationID, int broadcastInterval, JourneyRealizeHandler handler) {
        this.stationID = stationID;
        this.broadcastInterval = broadcastInterval;
        this.handler = handler;
        this.isOn = true;
    }

    @Override
    public void BTbroadcast() throws ConnectException {
        int i =0;
        try {

            if(isOn) {

                while (i<3) {
                    handler.broadcastStationID(stationID); //Error que es solucionara a posteriori en els canvis de JourneyRealizeHandler
                    TimeUnit.SECONDS.sleep(broadcastInterval); // Pausa entre emissions
                    i++;
                }
            }else{
                throw new ConnectException("No conectat");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurar estat d'interrupció
            throw new ConnectException("Emissió interrompuda.");
        } catch (Exception e) {
            throw new ConnectException("Error inesperat en la connexió Bluetooth.");
        }
    }

    public void setOn(Boolean on) {
        isOn = on;
    }
}
