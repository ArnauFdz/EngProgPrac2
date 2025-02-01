package services.smartfeatures;

import java.net.ConnectException;

/**
 * Representa el canal de comunicació Bluetooth que actua per defecte, sense necessitat de configuració prèvia.
 * Permet la connexió a qualsevol dispositiu Bluetooth proper i s'utilitza per descobrir les estacions
 * de vehicles a través de l'aplicació.
 *
 * Aquest canal Bluetooth no està restringit, a diferència del que s'utilitza per a la comunicació
 * amb els vehicles. La interfície encapsula un comportament continu que emet informació sobre
 * l'ID de l'estació mitjançant el mètode {@code broadcastStationID()} del controlador del cas d'ús.
 */
public interface UnbondedBTSignal {

    /**
     * Mètode encarregat de transmetre per Bluetooth l'ID de l'estació de manera continuada
     * i indefinida cada cert interval de temps.
     *
     * @throws ConnectException en cas que es produeixin fallades associades a la connexió Bluetooth.
     */
    void BTbroadcast() throws ConnectException;
    void setOn(Boolean on);
}
