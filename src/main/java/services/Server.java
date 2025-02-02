package services;

import exceptions.*;
import data.*;
import micromobility.JourneyService;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Interfície que defineix les operacions del servidor per gestionar vehicles i aparellaments.
 *
 * Relació:
 * - Implementada pel dummy `DummyServer`, que simula la interacció amb un servidor.
 * - Provada a `ServerTest` per assegurar el comportament correcte en registres, verificacions i aparellaments.
 */
public interface Server {
    /**
     * Verifica si un vehicle està disponible.
     *
     * @param vhID Identificador del vehicle.
     * @return
     * @throws PMVNotAvailException si el vehicle no està disponible.
     * @throws ConnectException     si hi ha un error de connexió.
     */
    Object checkPMVAvail(VehicleID vhID) throws PMVNotAvailException, ConnectException;

    /**
     * Registra un aparellament entre un usuari i un vehicle.
     * @param user L'usuari que realitza l'aparellament.
     * @param veh El vehicle a aparellar.
     * @param st L'estació associada.
     * @param loc La ubicació.
     * @param date La data i hora de l'aparellament.
     * @throws InvalidPairingArgsException si els arguments no són vàlids.
     * @throws ConnectException si hi ha un error de connexió.
     */
    void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc,
                         LocalDateTime date) throws InvalidPairingArgsException, ConnectException;

    /**
     * Finalitza un aparellament entre un usuari i un vehicle.
     * @param user L'usuari que finalitza l'aparellament.
     * @param veh El vehicle a desemparellar.
     * @param st L'estació associada.
     * @param loc La ubicació.
     * @param date La data i hora de finalització.
     * @param avSp Velocitat mitjana del viatge.
     * @param dist Distància recorreguda.
     * @param dur Durada del viatge.
     * @param imp Import del viatge.
     * @throws InvalidPairingArgsException si els arguments no són vàlids.
     * @throws ConnectException si hi ha un error de connexió.
     */
    void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc,
                     LocalDateTime date, float avSp, double dist, int dur, BigDecimal imp)
            throws InvalidPairingArgsException, ConnectException;
    void setAvailableVehicles(VehicleID vehicles);

    void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date);

    void unPairRegisterService(JourneyService s) throws PairingNotFoundException;

    void registerLocation(VehicleID veh, StationID st);

    void registerPayment(ServiceID servID, UserAccount user, BigDecimal imp, char payMeth) throws ConnectException;
}
