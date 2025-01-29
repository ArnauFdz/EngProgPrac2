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
     * Import del pagament.
     */
    private BigDecimal amount;

    /**
     * Constructor de la classe Payment.
     *
     * @param journeyService Servei associat al pagament.
     * @param amount Import del pagament.
     */
    public Payment(JourneyService journeyService, BigDecimal amount) {
        if (journeyService == null || amount == null) {
            throw new IllegalArgumentException("Els paràmetres no poden ser nuls.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'import ha de ser més gran que zero.");
        }
        this.journeyService = journeyService;
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

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "journeyService=" + journeyService +
                ", amount=" + amount +
                '}';
    }
}
