package micromobility.payment;

import micromobility.JourneyService;
import data.UserAccount;
import java.math.BigDecimal;

/**
 * Classe base per a la jerarquia de pagaments admesos pel sistema.
 */
public abstract class Payment {

    /**
     * Servei associat al pagament.
     */
    private JourneyService journeyService;

    /**
     * Usuari involucrat en el pagament.
     */
    private UserAccount userAcc;

    /**
     * Import del pagament.
     */
    private BigDecimal amount;

    /**
     * Constructor de la classe Payment.
     *
     * @param journeyService Servei associat al pagament.
     * @param userAcc Usuari que realitza el pagament.
     * @param amount Import del pagament.
     */
    public Payment(JourneyService journeyService, UserAccount userAcc, BigDecimal amount) {
        if (journeyService == null || userAcc == null || amount == null) {
            throw new IllegalArgumentException("Els paràmetres no poden ser nuls.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'import ha de ser més gran que zero.");
        }
        this.journeyService = journeyService;
        this.userAcc = userAcc;
        this.amount = amount;
    }

    /**
     * Realitza el pagament.
     * Aquest mètode ha de ser implementat per les subclasses.
     *
     * @throws Exception si ocorre un error durant el pagament.
     */
    public abstract void executePayment() throws Exception;

    // Getters i setters

    public JourneyService getJourneyService() {
        return journeyService;
    }

    public void setJourneyService(JourneyService journeyService) {
        if (journeyService == null) {
            throw new IllegalArgumentException("El servei no pot ser nul.");
        }
        this.journeyService = journeyService;
    }

    public UserAccount getUserAcc() {
        return userAcc;
    }

    public void setUserAcc(UserAccount userAcc) {
        if (userAcc == null) {
            throw new IllegalArgumentException("L'usuari no pot ser nul.");
        }
        this.userAcc = userAcc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'import ha de ser més gran que zero.");
        }
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "journeyService=" + journeyService +
                ", userAcc=" + userAcc +
                ", amount=" + amount +
                '}';
    }
}
