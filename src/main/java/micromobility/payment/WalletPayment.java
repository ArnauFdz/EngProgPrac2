package micromobility.payment;

import data.UserAccount;
import micromobility.JourneyService;
import java.math.BigDecimal;
import exceptions.*;
/**
 * Classe que gestiona els pagaments realitzats a través d'un moneder virtual.
 */
public class WalletPayment extends Payment {

    /**
     * Moneder associat al pagament.
     */
    private Wallet wallet;

    /**
     * Constructor de WalletPayment.
     *
     * @param service Servei associat al pagament.
     * @param user Usuari que realitza el pagament.
     * @param wallet Moneder associat.
     * @param amount Import del pagament.
     */
    public WalletPayment(JourneyService service, UserAccount user, Wallet wallet, BigDecimal amount) {
        super(service, user, amount);
        if (wallet == null) {
            throw new IllegalArgumentException("El moneder no pot ser nul.");
        }
        this.wallet = wallet;
    }

    /**
     * Retorna el moneder associat.
     *
     * @return El moneder.
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Comprova si hi ha fons suficients al moneder per cobrir l'import.
     *
     * @param amount Import a comprovar.
     * @return True si hi ha fons suficients, false en cas contrari.
     */
    public boolean hasSufficientFunds(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'import ha de ser més gran que zero.");
        }
        return wallet.getBalance().compareTo(amount) >= 0;
    }

    /**
     * Dedueix un import del moneder si hi ha fons suficients.
     *
     * @param amount Import a deduir.
     * @throws NotEnoughWalletException si no hi ha prou saldo.
     */
    public void deductAmount(BigDecimal amount) throws NotEnoughWalletException {
        if (hasSufficientFunds(amount)) {
            wallet.subBalance(amount);
            System.out.println("Pagament realitzat correctament: " + amount + " deduïts del moneder.");
        } else {
            throw new NotEnoughWalletException("Fons insuficients.");
        }
    }

    /**
     * Implementació del mètode abstracte de Payment.
     * Realitza el pagament deduint l'import del moneder.
     *
     * @throws NotEnoughWalletException si no hi ha prou saldo al moneder.
     */
    @Override
    public void executePayment() throws NotEnoughWalletException {
        deductAmount(getAmount());
    }
}
