package exceptions;

/**
 * Excepció llançada quan un codi QR està corrupte o il·legible.
 */
public class CorruptedImgException extends Exception {
    public CorruptedImgException(String message) {
        super(message);
    }
}
