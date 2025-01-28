package micromobility;

import data.GeographicPoint;
import data.VehicleID;

/**
 * Classe que representa un vehicle PMV amb informació sobre el seu estat i ubicació.
 */
public class PMVehicle {
    private VehicleID vehicleID; // Identificador únic del vehicle
    private PMVState state; // Estat actual del vehicle
    private GeographicPoint location; // Ubicació actual del vehicle

    /**
     * Constructor de PMVehicle.
     * @param vehicleID Identificador del vehicle. No pot ser nul.
     * @param initialState Estat inicial del vehicle. No pot ser nul.
     * @param initialLocation Ubicació inicial del vehicle. No pot ser nul·la.
     * @throws IllegalArgumentException si algun dels paràmetres és nul.
     */
    public PMVehicle(VehicleID vehicleID, PMVState initialState, GeographicPoint initialLocation) {
        if (vehicleID == null || initialState == null || initialLocation == null) {
            throw new IllegalArgumentException("VehicleID, estat i ubicació no poden ser nuls.");
        }
        this.vehicleID = vehicleID;
        this.state = initialState;
        this.location = initialLocation;
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
}
