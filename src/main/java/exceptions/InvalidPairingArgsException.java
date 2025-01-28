package exceptions;

/**
 * Excepció llançada quan els arguments per a l'aparellament són invàlids.
 */
public class InvalidPairingArgsException extends Exception {
    public InvalidPairingArgsException(String message) {
        super(message);
    }
}
