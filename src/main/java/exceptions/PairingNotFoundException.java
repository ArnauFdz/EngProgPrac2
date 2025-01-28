package exceptions;

/**
 * Excepció llançada quan no es troba un aparellament esperat.
 */
public class PairingNotFoundException extends Exception {
    public PairingNotFoundException(String message) {
        super(message);
    }
}
