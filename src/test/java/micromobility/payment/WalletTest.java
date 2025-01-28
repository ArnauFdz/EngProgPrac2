package micromobility.payment;

import exceptions.NotEnoughWalletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {
    private Wallet wallet; // Instància de moneder per als tests

    @BeforeEach
    void setUp() {
        // Inicialitza el moneder abans de cada test
        wallet = new Wallet(new BigDecimal("100.00"));
    }

    @Test
    void testWalletInitialization() {
        // Comprova que el moneder s'inicialitza amb el saldo correcte
        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
    }

    @Test
    void testAddFunds() {
        // Comprova que es poden afegir fons al moneder
        wallet.addFunds(new BigDecimal("25.00"));
        assertEquals(new BigDecimal("125.00"), wallet.getBalance());
    }

    @Test
    void testSubBalance() throws NotEnoughWalletException {
        // Comprova que es poden deduir fons del moneder
        wallet.subBalance(new BigDecimal("30.00"));
        assertEquals(new BigDecimal("70.00"), wallet.getBalance());
    }

    @Test
    void testSubBalanceInsufficientFunds() {
        // Configura un moneder amb saldo insuficient per aquest test
        wallet = new Wallet(new BigDecimal("20.00"));
        // Comprova que no es poden deduir fons si no hi ha prou saldo
        assertThrows(NotEnoughWalletException.class, () -> wallet.subBalance(new BigDecimal("30.00")));
    }

    @Test
    void testAddNegativeFunds() {
        // Comprova que no es poden afegir fons negatius
        assertThrows(IllegalArgumentException.class, () -> wallet.addFunds(new BigDecimal("-10.00")));
    }

    @Test
    void testSubBalanceNegativeAmount() {
        // Comprova que no es poden deduir fons amb un import negatiu
        assertThrows(IllegalArgumentException.class, () -> wallet.subBalance(new BigDecimal("-10.00")));
    }
}
