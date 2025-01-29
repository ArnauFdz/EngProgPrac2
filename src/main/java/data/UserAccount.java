// Fitxer: UserAccount.java
package data;

import micromobility.payment.Wallet;

/**
 * Classe UserAccount representa un identificador únic per a un compte d'usuari.
 * Aquesta classe és immutable i assegura que cada compte tingui un nom d'usuari vàlid.
 */
public final class UserAccount {
    // El nom d'usuari associat al compte, ha de ser únic i complir amb certs criteris.
    private final String username;
    private Wallet wallet;

    /**
     * Constructor de UserAccount.
     * @param username El nom d'usuari per al compte. No pot ser nul ni buit i ha de complir un format vàlid.
     * @throws IllegalArgumentException si el nom d'usuari no és vàlid.
     */
    public UserAccount(String username) {
        // Validació: El nom d'usuari no pot ser nul ni estar buit.
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nom d'usuari no pot ser nul o buit.");
        }
        // Validació: El nom d'usuari ha de contenir entre 3 i 20 caràcters alfanumèrics o els símbols permesos.
        if (!username.matches("[a-zA-Z0-9._-]{3,20}")) {
            throw new IllegalArgumentException("El nom d'usuari ha de tenir entre 3 i 20 caràcters alfanumèrics, punts, guions o guions baixos.");
        }
        // Si passa totes les validacions, assignem el nom d'usuari.
        this.username = username;
    }

    /**
     * Mètode per obtenir el nom d'usuari associat al compte.
     * @return El nom d'usuari.
     */
    public String getUsername() {
        return username;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     * Compara aquesta instància de UserAccount amb un altre objecte.
     * @param o L'objecte a comparar.
     * @return true si ambdós objectes tenen el mateix nom d'usuari, altrament false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Si són el mateix objecte, són iguals.
        if (o == null || getClass() != o.getClass()) return false; // Si l'objecte és d'una altra classe, no són iguals.
        UserAccount that = (UserAccount) o;
        return username.equals(that.username); // Compara els noms d'usuari.
    }

    /**
     * Genera un codi hash únic per a aquesta instància de UserAccount.
     * @return El codi hash basat en el nom d'usuari.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /**
     * Retorna una representació en forma de cadena de l'objecte UserAccount.
     * @return Una cadena que representa el compte d'usuari.
     */
    @Override
    public String toString() {
        return "UserAccount{" + "username='" + username + '\'' + '}';
    }
}
