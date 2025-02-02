package services;

import data.*;
import exceptions.*;
import micromobility.JourneyService;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SimulatedServer: Representa una implementació simulada del servei Server
 * per ser utilitzada en proves. Valida arguments i registra dades per
 * simular el comportament real del servidor.
 */
public class ServerImp implements Server {

    private UserAccount currentUser;
    private VehicleID currentVehicle;
    private StationID currentStation;
    private GeographicPoint currentLocation;
    private LocalDateTime pairingDate;


    private final Map<VehicleID, JourneyService> simulatedDatabase = new HashMap<>();
    private final Set<VehicleID> availableVehicles = new HashSet<>();
    private final Map<VehicleID, StationID> vehicleLocations = new HashMap<>();

    public ServerImp() {

    }
    @Override
    public void setAvailableVehicles(VehicleID vehicles) {
        availableVehicles.clear();
        availableVehicles.add(vehicles);
    }
    @Override
    public Object checkPMVAvail(VehicleID vhID) throws PMVNotAvailException, ConnectException {

        if (!availableVehicles.contains(vhID)) {
            throw new PMVNotAvailException("El vehicle no està disponible.");
        }

        System.out.println("SimulatedServer: checkPMVAvail executat per VehicleID: " + vhID.getId());
        return null;
    }

    @Override
    public void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date)
            throws InvalidPairingArgsException, ConnectException {


        if (user == null || veh == null || st == null || loc == null || date == null) {
            throw new InvalidPairingArgsException("Arguments no vàlids per registrar l'aparellament.");
        }

        if (!availableVehicles.contains(veh)) {
            throw new InvalidPairingArgsException("El vehicle no es troba disponible per emparellar.");
        }

        // Crear un identificador únic de servei
        ServiceID serviceID = new ServiceID("SRV_" + veh.getId() + "_" + date.toLocalTime().toSecondOfDay());

        // Crear una nova instància de JourneyService
        JourneyService journey = new JourneyService( user, veh);
        journey.setServiceInit(st, loc); // Iniciar el servei

        // Emmagatzemar el viatge a la base de dades simulada
        simulatedDatabase.put(veh, journey);

        // Marcar el vehicle com no disponible
        availableVehicles.remove(veh);

        System.out.println("SimulatedServer: registerPairing executat per VehicleID: " + veh.getId());
    }


    @Override
    public void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date,
                            float avSp, double dist, int dur, BigDecimal imp)
            throws InvalidPairingArgsException, ConnectException {


        if (user == null || veh == null || st == null || loc == null || date == null) {
            throw new InvalidPairingArgsException("Arguments no vàlids per aturar l'aparellament.");
        }

        if (dist <= 0 || dur <= 0 || imp.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPairingArgsException("Dades del servei no vàlides.");
        }

        // Recuperar el viatge en curs des de la base de dades simulada
        JourneyService journey = simulatedDatabase.get(veh);

        if (journey == null || !journey.isActive()) {
            throw new InvalidPairingArgsException("No hi ha cap viatge actiu per aquest vehicle.");
        }

        // Actualitzar la informació del viatge
        journey.setEndStation(st); // Assignar l'estació de finalització
        journey.setServiceFinish(st, loc, date, dist,avSp,dur,imp); // Finalitzar el server

        // Marcar el vehicle com disponible
        availableVehicles.add(veh);

        // Actualitzar la ubicació del vehicle
        registerLocation(veh, st);

        System.out.printf("Registre del servei completat: Vehicle %s marcat com a disponible.%n", veh.getId());
    }

    @Override
    public void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) {
        if (user == null || veh == null || st == null || loc == null || date == null) {
            throw new IllegalArgumentException("Arguments no vàlids per establir l'emparellament.");
        }

        // Crear un identificador únic de servei
        ServiceID serviceID = new ServiceID("SRV_" + veh.getId() + "_" + date.toLocalTime().toSecondOfDay());

        // Crear una instància de JourneyService
        JourneyService journey = new JourneyService( user, veh);
        journey.setServiceInit(st,loc); // Iniciar el servei

        // Guardar l'emparellament al servidor
        simulatedDatabase.put(veh, journey);

        // Actualitzar l'estat del vehicle (marcar com no disponible)
        availableVehicles.remove(veh);

        System.out.printf(
                "Pairing establert: Usuari %s, Vehicle %s, Estació %s, Ubicació %s, Data %s.%n",
                user.getUsername(), veh.getId(), st.getId(), loc, date
        );
    }

    @Override
    public void unPairRegisterService(JourneyService s) throws PairingNotFoundException {
        if (s == null || !s.isActive()) {
            throw new PairingNotFoundException("No s'ha trobat informació associada al servei proporcionat.");
        }

        VehicleID vehicleID = s.getVehicle();
        System.out.println("VehicleID from JourneyService: " + vehicleID);
        System.out.println("Simulated Database keys: " + simulatedDatabase.keySet());

        if (!simulatedDatabase.containsKey(vehicleID)) {
            throw new PairingNotFoundException("No existeix cap emparellament associat al vehicle ID: " + vehicleID.getId());
        }

        // Eliminar el viatge de la base de dades simulada
        simulatedDatabase.remove(vehicleID);

        // Marcar el vehicle com a disponible
        availableVehicles.add(vehicleID);

        System.out.printf(
                "SimulatedServer: unPairRegisterService executat amb èxit.%nVehicle ID: %s, Usuari: %s.%n",
                vehicleID.getId(), s.getUser().getUsername()
        );
    }


    @Override
    public void registerLocation(VehicleID veh, StationID st) {
        if (st == null) {
            throw new IllegalArgumentException("StationID no pot ser nul.");
        }

        if (veh != null) {
            vehicleLocations.put(veh, st);
            System.out.printf("SimulatedServer: registerLocation executat per Vehicle %s a Estació %s.%n", veh.getId(), st.getId());
        } else {
            System.out.printf("SimulatedServer: registerLocation executat per Estació %s sense VehicleID.%n", st.getId());
        }
    }


    // Mètode per consultar l'última estació registrada per a un vehicle
    public StationID getLocation(VehicleID veh) {
        StationID stationID = vehicleLocations.get(veh);
        return stationID;
    }

    public Map<VehicleID, JourneyService> getSimulatedDatabase() {
        return new HashMap<>(simulatedDatabase); // Per poder fer el test
    }

    public Set<VehicleID> getAvailableVehicles() {
        return new HashSet<>(availableVehicles); // Retorna una còpia per fer el test
    }

    @Override
    public void registerPayment(ServiceID servID, UserAccount user, BigDecimal imp, char payMeth) throws ConnectException {
        if (servID == null || user == null || imp == null) {
            throw new IllegalArgumentException("Els paràmetres no poden ser nuls.");
        }
        if (payMeth != 'W' && payMeth != 'P' && payMeth != 'T' && payMeth != 'B') {
            throw new IllegalArgumentException("Mètode de pagament invàlid. S'esperen 'W' (wallet), 'P' (passarel·la), 'T' (targeta) o 'B' (Bizum).");
        }

        // Simulació d'un problema de connexió
        if ("ERROR_CON".equals(servID.getId())) {
            throw new ConnectException("Error de connexió amb el servidor.");
        }

        System.out.println("Pagament registrat correctament:");
        System.out.println("ServiceID: " + servID.getId());
        System.out.println("Usuari: " + user.getUsername());
        System.out.println("Import: " + imp);
        System.out.println("Mètode de pagament: " + payMeth);
    }



}
