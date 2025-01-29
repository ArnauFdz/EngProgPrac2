// Fitxer: VehicleID.java
package data;

/**
 * Classe VehicleID representa un identificador únic per a un vehicle.
 * Aquesta classe és immutable i assegura que cada vehicle tingui un ID vàlid.
 */
public final class VehicleID {
    // L'identificador del vehicle, ha de complir amb certs criteris.
    private final String id;

    /**
     * Constructor de VehicleID.
     * @param id L'identificador per al vehicle. No pot ser nul ni buit i ha de complir un format vàlid.
     * @throws IllegalArgumentException si l'identificador no és vàlid.
     */
    public VehicleID(String id) {
        // Validació: L'ID no pot ser nul ni estar buit.
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del vehicle no pot ser nul o buit.");
        }
        // Validació: L'ID ha de contenir entre 6 i 15 caràcters alfanumèrics.
        if (!id.matches("[A-Z0-9]{6,15}")) {
            throw new IllegalArgumentException("L'ID del vehicle ha de tenir entre 6 i 15 caràcters alfanumèrics.");
        }
        // Si passa totes les validacions, assignem l'ID.
        this.id = id;
    }

    /**
     * Mètode per obtenir l'ID del vehicle.
     * @return L'identificador del vehicle.
     */
    public String getId() {
        return id;
    }

    /**
     * Compara aquesta instància de VehicleID amb un altre objecte.
     * @param o L'objecte a comparar.
     * @return true si ambdós objectes tenen el mateix ID, altrament false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Si són el mateix objecte, són iguals.
        if (o == null || getClass() != o.getClass()) return false; // Si l'objecte és d'una altra classe, no són iguals.
        VehicleID vehicleID = (VehicleID) o;
        return id.equals(vehicleID.id); // Compara els IDs.
    }

    /**
     * Genera un codi hash únic per a aquesta instància de VehicleID.
     * @return El codi hash basat en l'ID del vehicle.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Retorna una representació en forma de cadena de l'objecte VehicleID.
     * @return Una cadena que representa l'ID del vehicle.
     */
    @Override
    public String toString() {
        return "VehicleID{" + "id='" + id + '\'' + '}';
    }
}
