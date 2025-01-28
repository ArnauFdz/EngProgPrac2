package exceptions;

/**
 * Excepció llançada quan ocorre un error en el flux del procediment.
 */
public class ProceduralException extends Exception {
    public ProceduralException(String message) {
        super(message);
    }
}
