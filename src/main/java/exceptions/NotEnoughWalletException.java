package exceptions;

/**
 * Excepció que s'utilitza per indicar que no hi ha suficients fons al moneder.
 */
public class NotEnoughWalletException extends Exception {

    /**
     * Constructor per defecte.
     */
    public NotEnoughWalletException(String message) {

        super(message);
    }
}
