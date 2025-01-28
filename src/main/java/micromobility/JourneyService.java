package micromobility;

import data.ServiceID;
import data.StationID;
import data.UserAccount;
import data.VehicleID;

import java.time.LocalDateTime;

public class JourneyService {

    // Membres de la classe
    private final ServiceID serviceID; // Identificador únic del servei
    private final UserAccount user; // Usuari que realitza el viatge
    private final VehicleID vehicle; // Vehicle utilitzat
    private final StationID startStation; // Estació d'inici
    private StationID endStation; // Estació de finalització
    private LocalDateTime startTime; // Hora d'inici
    private LocalDateTime endTime; // Hora de finalització
    private boolean active; // Estat del servei (actiu o finalitzat)

    // Constructor
    public JourneyService(ServiceID serviceID, UserAccount user, VehicleID vehicle, StationID startStation) {
        if (serviceID == null || user == null || vehicle == null || startStation == null) {
            throw new IllegalArgumentException("Els paràmetres no poden ser nuls.");
        }
        this.serviceID = serviceID;
        this.user = user;
        this.vehicle = vehicle;
        this.startStation = startStation;
        this.active = false; // Per defecte, no actiu fins que s'inicia
    }

    // Getters
    public ServiceID getServiceID() {
        return serviceID;
    }

    public UserAccount getUser() {
        return user;
    }

    public VehicleID getVehicle() {
        return vehicle;
    }

    public StationID getStartStation() {
        return startStation;
    }

    public StationID getEndStation() {
        return endStation;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setEndStation(StationID endStation) {
        if (endStation == null) {
            throw new IllegalArgumentException("L'estació de finalització no pot ser nul·la.");
        }
        this.endStation = endStation;
    }

    public void setServiceInit() {
        if (active) {
            throw new IllegalStateException("El servei ja està actiu.");
        }
        this.startTime = LocalDateTime.now();
        this.active = true;
        System.out.println("Servei iniciat a: " + startTime);
    }

    public void setServiceFinish() {
        if (!active) {
            throw new IllegalStateException("El servei no està actiu i no es pot finalitzar.");
        }
        this.endTime = LocalDateTime.now();
        this.active = false;
        System.out.println("Servei finalitzat a: " + endTime);
    }
}
