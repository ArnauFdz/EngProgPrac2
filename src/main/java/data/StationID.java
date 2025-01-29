// Fitxer: StationID.java
package data;

/**
 * Classe StationID representa un identificador únic per a una estació.
 * Aquesta classe és immutable i assegura que cada estació tingui un ID vàlid.
 */
public final class StationID {
    // L'identificador de l'estació, ha de complir amb certs criteris.
    private final String id;

    /**
     * Constructor de StationID.
     * @param id L'identificador per a l'estació. No pot ser nul ni buit i ha de complir un format vàlid.
     * @throws IllegalArgumentException si l'identificador no és vàlid.
     */
    public StationID(String id) {
        // Validació: L'ID no pot ser nul ni estar buit.
        if (id == null || id.trim().isEmpty()) { //Trim elimina els espais
            throw new IllegalArgumentException("L'ID de l'estació no pot ser nul o buit.");
        }
        // Validació: L'ID ha de contenir entre 5 i 20 caràcters alfanumèrics, guions o guions baixos.
        if (!id.matches("[A-Z0-9_-]{5,20}")) {
            throw new IllegalArgumentException("L'ID de l'estació ha de tenir entre 5 i 20 caràcters alfanumèrics, guions o guions baixos.");
        }
        // Si passa totes les validacions, assignem l'ID.
        this.id = id;
    }

    /**
     * Mètode per obtenir l'ID de l'estació.
     * @return L'identificador de l'estació.
     */
    public String getId() {
        return id;
    }

    /**
     * Compara aquesta instància de StationID amb un altre objecte.
     * @param o L'objecte a comparar.
     * @return true si ambdós objectes tenen el mateix ID, altrament false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Si són el mateix objecte, són iguals.
        if (o == null || getClass() != o.getClass()) return false; // Si l'objecte és d'una altra classe, no són iguals.
        StationID stationID = (StationID) o;
        return id.equals(stationID.id); // Compara els IDs.
    }

    /**
     * Genera un codi hash únic per a aquesta instància de StationID.
     * @return El codi hash basat en l'ID de l'estació.
     */
    @Override
    public int hashCode() {
        return id.hashCode(); //Funció simplificada
    }

    /**
     * Retorna una representació en forma de cadena de l'objecte StationID.
     * @return Una cadena que representa l'ID de l'estació.
     */
    @Override
    public String toString() {
        return "StationID{" + "id='" + id + '\'' + '}';
    }
}
