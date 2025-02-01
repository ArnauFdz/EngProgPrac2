package data;

import micromobility.payment.Wallet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserAccountTest {

    @Test
    void testValidUserAccount() {
        UserAccount user = new UserAccount("user_123");
        assertEquals("user_123", user.getUsername());
        assertNull(user.getWallet());
    }

    @Test
    void testNullUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount(null);
        });
        assertEquals("El nom d'usuari no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testEmptyUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("");
        });
        assertEquals("El nom d'usuari no pot ser nul o buit.", exception.getMessage());
    }

    @Test
    void testInvalidFormatUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("us@r!");
        });
        assertEquals("El nom d'usuari ha de tenir entre 3 i 20 caràcters alfanumèrics, punts, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testTooShortUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("ab");
        });
        assertEquals("El nom d'usuari ha de tenir entre 3 i 20 caràcters alfanumèrics, punts, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testTooLongUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new UserAccount("a".repeat(21));
        });
        assertEquals("El nom d'usuari ha de tenir entre 3 i 20 caràcters alfanumèrics, punts, guions o guions baixos.", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        UserAccount user1 = new UserAccount("uniqueUser");
        UserAccount user2 = new UserAccount("uniqueUser");
        UserAccount user3 = new UserAccount("differentUser");

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        UserAccount user = new UserAccount("testUser");
        assertEquals("UserAccount{username='testUser'}", user.toString());
    }
}
