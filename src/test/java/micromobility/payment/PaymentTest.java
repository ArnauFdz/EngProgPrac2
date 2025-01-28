package micromobility.payment;

import data.ServiceID;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import micromobility.JourneyService;
import org.junit.jupiter.api.Test;
import data.GeographicPoint;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testPaymentCreationSuccess() {
        // Crea objectes necessaris per al test
        ServiceID serviceID = new ServiceID("service123");
        UserAccount user = new UserAccount("user123");
        VehicleID vehicle = new VehicleID("VEH12345"); // Ajustat per complir amb els criteris
        GeographicPoint ubicacio = new GeographicPoint(41.40338f, 2.17403f);
        StationID startStation = new StationID("STATION_12345", ubicacio); // Ajustat per complir amb els criteris
        JourneyService journeyService = new JourneyService(serviceID, user, vehicle, startStation);

        // Utilitza WalletPayment com a implementació de Payment
        Payment payment = new WalletPayment(journeyService, user, new Wallet(new BigDecimal("50.00")), new BigDecimal("10.00"));

        // Verifica que els atributs comuns de Payment es configuren correctament
        assertEquals(new BigDecimal("10.00"), payment.getAmount());
        assertEquals(user, payment.getUserAcc());
        assertEquals(journeyService, payment.getJourneyService());
    }

    @Test
    void testPaymentCreationFailure() {
        // Crea objectes necessaris per al test
        ServiceID serviceID = new ServiceID("service123");
        UserAccount user = new UserAccount("user123");
        VehicleID vehicle = new VehicleID("VEH12345"); // Ajustat
        GeographicPoint ubicacio = new GeographicPoint(41.40338f, 2.17403f);
        StationID startStation = new StationID("STATION_12345", ubicacio); // Ajustat
        JourneyService journeyService = new JourneyService(serviceID, user, vehicle, startStation);
        Wallet wallet = new Wallet(new BigDecimal("50.00"));

        // Comprova que es llencen excepcions per paràmetres no vàlids
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(null, user, wallet, new BigDecimal("10.00")));
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(journeyService, null, wallet, new BigDecimal("10.00")));
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(journeyService, user, null, new BigDecimal("10.00")));
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(journeyService, user, wallet, BigDecimal.ZERO));
    }
}
