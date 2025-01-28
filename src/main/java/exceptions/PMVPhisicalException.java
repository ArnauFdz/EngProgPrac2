package exceptions;

/**
 * Excepció que indica problemes tècnics en el vehicle.
 */
public class PMVPhisicalException extends Exception {
    public PMVPhisicalException(String message) {
        super(message);
    }
}
