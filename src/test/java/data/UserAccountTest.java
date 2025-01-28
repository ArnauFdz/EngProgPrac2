package data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de proves per a la classe UserAccount.
 */
public class UserAccountTest {

    /**
     * Comprova que es llença una excepció quan el nom d'usuari és nul.
     */
    @Test
    public void testUsernameNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount(null);
        });
        assertEquals("El nom d'usuari no pot ser nul o buit.", exception.getMessage());
    }

    /**
     * Comprova que es llença una excepció quan el nom d'usuari és buit.
     */
    @Test
    public void testUsernameEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("");
        });
        assertEquals("El nom d'usuari no pot ser nul o buit.", exception.getMessage());
    }

    /**
     * Comprova que es llença una excepció quan el nom d'usuari només conté espais en blanc.
     */
    @Test
    public void testUsernameSpacesOnly() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("   ");
        });
        assertEquals("El nom d'usuari no pot ser nul o buit.", exception.getMessage());
    }

    /**
     * Comprova que es llença una excepció quan el nom d'usuari és massa curt.
     */
    @Test
    public void testUsernameTooShort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("ab");
        });
        assertEquals("El nom d'usuari ha de tenir entre 3 i 20 caràcters alfanumèrics, punts, guions o guions baixos.", exception.getMessage());
    }

    /**
     * Comprova que es llença una excepció quan el nom d'usuari és massa llarg.
     */
    @Test
    public void testUsernameTooLong() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("aVeryLongUsernameThatExceedsTwentyCharacters");
        });
        assertEquals("El nom d'usuari ha de tenir entre 3 i 20 caràcters alfanumèrics, punts, guions o guions baixos.", exception.getMessage());
    }

    /**
     * Comprova que es llença una excepció quan el nom d'usuari conté caràcters no vàlids.
     */
    @Test
    public void testUsernameInvalidCharacters() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("user@name");
        });
        assertEquals("El nom d'usuari ha de tenir entre 3 i 20 caràcters alfanumèrics, punts, guions o guions baixos.", exception.getMessage());
    }

    /**
     * Comprova que es pot crear correctament un nom d'usuari vàlid.
     */
    @Test
    public void testValidUsername() {
        UserAccount account = new UserAccount("valid.username_123");
        assertEquals("valid.username_123", account.getUsername());
    }

    /**
     * Comprova que dos objectes UserAccount amb el mateix nom d'usuari són iguals.
     */
    @Test
    public void testEquals() {
        UserAccount account1 = new UserAccount("username123");
        UserAccount account2 = new UserAccount("username123");
        assertEquals(account1, account2);
    }

    /**
     * Comprova que el hashCode es genera correctament per a dos objectes iguals.
     */
    @Test
    public void testHashCode() {
        UserAccount account1 = new UserAccount("username123");
        UserAccount account2 = new UserAccount("username123");
        assertEquals(account1.hashCode(), account2.hashCode());
    }

    /**
     * Comprova la representació en forma de cadena de l'objecte UserAccount.
     */
    @Test
    public void testToString() {
        UserAccount account = new UserAccount("username123");
        assertEquals("UserAccount{username='username123'}", account.toString());
    }
}