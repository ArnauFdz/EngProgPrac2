package data;

/**
 * Classe ServiceID representa un identificador únic per a un servei.
 * És immutable i assegura que cada servei tingui un ID vàlid.
 */
public final class ServiceID {
    private final String id;

    /**
     * Constructor de ServiceID.
     * @param id L'identificador únic del servei. Ha de complir amb un format vàlid.
     * @throws IllegalArgumentException si l'ID és nul, buit o no compleix amb el patró esperat.
     */
    public ServiceID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del servei no pot ser nul o buit.");
        }
        // Patró d'exemple: alfanumèric amb longitud entre 6 i 30 caràcters
        if (!id.matches("[A-Za-z0-9_-]{6,30}")) {
            throw new IllegalArgumentException("L'ID del servei ha de tenir entre 6 i 30 caràcters alfanumèrics, guions o guions baixos.");
        }
        this.id = id;
    }

    /**
     * Mètode per obtenir l'ID del servei.
     * @return L'identificador del servei.
     */
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq;
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ServiceID serviceID = (ServiceID) obj;
        return id.equals(serviceID.id);
    }

    @Override
    public int hashCode() {
        // Genera un codi hash únic basat en el valor de 'id'.
        return id.hashCode(); // Utilitza el mètode hashCode de la classe String.
    }

    @Override
    public String toString() {
        // Retorna una representació textual de l'objecte ServiceID.
        return "ServiceID{" + "id='" + id + '\'' + '}';
    }
}
