package exceptions;

/**
 * Excepció llançada quan un PMV no està disponible.
 */
public class PMVNotAvailException extends Exception {
    public PMVNotAvailException(String message) {
        super(message);
    }
}
