package micromobility.payment;

import data.ServiceID;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import exceptions.NotEnoughWalletException;
import micromobility.JourneyService;
import org.junit.jupiter.api.Test;
import data.GeographicPoint;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletPaymentTest {

    @Test
    void testWalletPaymentCreationSuccess() {
        // Crea objectes necessaris per al test
        ServiceID serviceID = new ServiceID("service123");
        UserAccount user = new UserAccount("user123");
        VehicleID vehicle = new VehicleID("VEH12345");
        GeographicPoint ubicacio = new GeographicPoint(41.40338f, 2.17403f);
        StationID startStation = new StationID("STATION_12345", ubicacio); // ID ajustat
        JourneyService journeyService = new JourneyService(serviceID, user, vehicle, startStation);
        Wallet wallet = new Wallet(new BigDecimal("50.00"));

        // Crea un WalletPayment amb dades vàlides
        WalletPayment walletPayment = new WalletPayment(journeyService, user, wallet, new BigDecimal("10.00"));

        // Comprova que el WalletPayment es crea correctament
        assertEquals(new BigDecimal("10.00"), walletPayment.getAmount());
        assertEquals(wallet, walletPayment.getWallet());
        assertEquals(user, walletPayment.getUserAcc());
        assertEquals(journeyService, walletPayment.getJourneyService());
    }

    @Test
    void testWalletPaymentCreationFailure() {
        // Crea objectes necessaris per al test
        ServiceID serviceID = new ServiceID("service123");
        UserAccount user = new UserAccount("user123");
        VehicleID vehicle = new VehicleID("VEH12345");
        GeographicPoint ubicacio = new GeographicPoint(41.40338f, 2.17403f);
        StationID startStation = new StationID("STATION_12345", ubicacio); // ID ajustat
        JourneyService journeyService = new JourneyService(serviceID, user, vehicle, startStation);
        Wallet wallet = new Wallet(new BigDecimal("50.00"));

        // Comprova que es llencen excepcions per paràmetres no vàlids
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(null, user, wallet, new BigDecimal("10.00")));
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(journeyService, null, wallet, new BigDecimal("10.00")));
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(journeyService, user, null, new BigDecimal("10.00")));
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(journeyService, user, wallet, BigDecimal.ZERO));
    }

    @Test
    void testExecutePaymentSuccess() throws Exception {
        // Crea objectes necessaris per al test
        ServiceID serviceID = new ServiceID("service123");
        UserAccount user = new UserAccount("user123");
        VehicleID vehicle = new VehicleID("VEH12345");
        GeographicPoint ubicacio = new GeographicPoint(41.40338f, 2.17403f);
        StationID startStation = new StationID("STATION_12345", ubicacio); // ID ajustat
        JourneyService journeyService = new JourneyService(serviceID, user, vehicle, startStation);
        Wallet wallet = new Wallet(new BigDecimal("50.00"));

        // Crea un WalletPayment
        WalletPayment walletPayment = new WalletPayment(journeyService, user, wallet, new BigDecimal("10.00"));

        // Executa el pagament
        walletPayment.executePayment();

        // Verifica que el saldo s'ha reduït correctament
        assertEquals(new BigDecimal("40.00"), wallet.getBalance());
    }

    @Test
    void testExecutePaymentFailureInsufficientFunds() {
        // Crea objectes necessaris per al test
        ServiceID serviceID = new ServiceID("service123");
        UserAccount user = new UserAccount("user123");
        VehicleID vehicle = new VehicleID("VEH12345");
        GeographicPoint ubicacio = new GeographicPoint(41.40338f, 2.17403f);
        StationID startStation = new StationID("STATION_12345", ubicacio); // ID ajustat
        JourneyService journeyService = new JourneyService(serviceID, user, vehicle, startStation);
        Wallet wallet = new Wallet(new BigDecimal("5.00"));

        // Crea un WalletPayment amb import superior al saldo disponible
        WalletPayment walletPayment = new WalletPayment(journeyService, user, wallet, new BigDecimal("10.00"));

        // Comprova que es llenca NotEnoughWalletException
        assertThrows(NotEnoughWalletException.class, walletPayment::executePayment);
    }
}
