package micromobility.payment;

import java.math.BigDecimal;
import exceptions.*;
/**
 * Classe que representa el moneder de l'app.
 */
public class Wallet {

    /**
     * Saldo disponible al moneder.
     */
    private BigDecimal balance;

    /**
     * Constructor del moneder.
     *
     * @param initialBalance Saldo inicial del moneder.
     * @throws IllegalArgumentException si el saldo inicial és negatiu.
     */
    public Wallet(BigDecimal initialBalance) {
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial no pot ser negatiu o nul.");
        }
        this.balance = initialBalance;
    }

    /**
     * Deducció d'un import del moneder.
     *
     * @param amount Import a deduir.
     * @throws NotEnoughWalletException si no hi ha prou saldo disponible.
     */
    public void subBalance(BigDecimal amount) throws NotEnoughWalletException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'import a deduir ha de ser més gran que zero.");
        }
        if (amount.compareTo(balance) > 0) {
            throw new NotEnoughWalletException("Saldo insuficient al moneder.");
        }
        balance = balance.subtract(amount);
    }

    /**
     * Obté el saldo actual del moneder.
     *
     * @return El saldo disponible.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Afegeix saldo al moneder.
     *
     * @param amount Saldo a afegir.
     * @throws IllegalArgumentException si l'import és negatiu o nul.
     */
    public void addFunds(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'import a afegir ha de ser més gran que zero.");
        }
        balance = balance.add(amount);
    }
}
