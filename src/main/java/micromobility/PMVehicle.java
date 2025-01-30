package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;

/**
 * Classe que representa un vehicle PMV amb informació sobre el seu estat i ubicació.
 */
public class PMVehicle {
    private final VehicleID vehicleID; // Identificador únic del vehicle
    private PMVState state; // Estat actual del vehicle
    private GeographicPoint location; // Ubicació actual del vehicle
    private StationID stationID;
    private UserAccount user;

    /**
     * Constructor de PMVehicle.
     * @param vehicleID Identificador del vehicle. No pot ser nul.
     * @param initialLocation Ubicació inicial del vehicle. No pot ser nul·la.
     * @throws IllegalArgumentException si algun dels paràmetres és nul.
     */
    public PMVehicle(VehicleID vehicleID, GeographicPoint initialLocation, StationID stationID, UserAccount user) {
        if (vehicleID == null || initialLocation == null) {
            throw new IllegalArgumentException("VehicleID estat i ubicació no poden ser nuls.");
        }
        this.vehicleID = vehicleID;
        this.location = initialLocation;
        this.stationID = stationID;
        this.state =PMVState.Available;
        this.user = user;
    }

    /**
     * Marca el vehicle com no disponible.
     */
    public void setNotAvailb() {
        this.state = PMVState.NotAvailable;
    }

    /**
     * Marca el vehicle perque estigui en ús.
     */
    public void setUnderWay() {
        this.state = PMVState.UnderWay;
    }

    /**
     * Marca el vehicle com disponible.
     */
    public void setAvailb() {
        this.state = PMVState.Available;
    }

    /**
     * Actualitza la ubicació del vehicle.
     * @param newLocation Nova ubicació del vehicle. No pot ser nul·la.
     * @throws IllegalArgumentException si la nova ubicació és nul·la.
     */
    public void setLocation(GeographicPoint newLocation) {
        if (newLocation == null) {
            throw new IllegalArgumentException("L'ubicació no pot ser nul·la.");
        }
        this.location = newLocation;
    }

    public void setState(PMVState state) {
        this.state = state;
    }

    public void setStationID(StationID stationID) {
        this.stationID = stationID;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }


    /**
     * Retorna l'identificador del vehicle.
     * @return VehicleID del vehicle.
     */
    public VehicleID getVehicleID() {
        return vehicleID;
    }

    /**
     * Retorna l'estat actual del vehicle.
     * @return Estat del vehicle.
     */
    public PMVState getState() {
        return state;
    }

    /**
     * Retorna l'ubicació actual del vehicle.
     * @return Ubicació del vehicle.
     */
    public GeographicPoint getLocation() {
        return location;
    }

    public StationID getStationID(){
        return stationID;
    }

    public UserAccount getUser(){return user;}
}
