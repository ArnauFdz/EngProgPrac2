package micromobility;

import data.*;
import exceptions.*;
import services.*;
import micromobility.payment.*;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.QRDecoder;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Classe que gestiona la lògica d'un viatge utilitzant un vehicle PMV.
 */
public class JourneyRealizeHandler {
    private final Server server;
    private final QRDecoder qrDecoder;
    private final ArduinoMicroController microController;
    private boolean isDriving; // Estat intern per verificar si està en conducció
    private final Wallet wallet;

    private final JourneyService journey;

    public JourneyRealizeHandler(Server server, QRDecoder qrDecoder, ArduinoMicroController microController, Wallet wallet, JourneyService journey) {
        if (server == null || qrDecoder == null || microController == null || wallet == null) {
            throw new IllegalArgumentException("Les dependències no poden ser nul·les.");
        }
        this.server = server;
        this.qrDecoder = qrDecoder;
        this.microController = microController;
        this.wallet = wallet;
        this.isDriving = false; // Inicialització de l'estat
        this.journey = journey;
    }

    public boolean isDriving() {
        return isDriving;
    }

    public void scanQR(BufferedImage qrImage)
            throws ConnectException, InvalidPairingArgsException, CorruptedImgException, PMVNotAvailException, ProceduralException {
        if (qrImage == null) {
            throw new ProceduralException("La imatge QR no pot ser nul·la.");
        }

        VehicleID vehicleID = qrDecoder.getVehicleID(qrImage);
        server.checkPMVAvail(vehicleID);
    }

    public void unPairVehicle(JourneyService journeyService)
            throws ConnectException, InvalidPairingArgsException, PairingNotFoundException, ProceduralException {
        if (journeyService == null || !journeyService.isActive()) {
            throw new ProceduralException("El servei d'aparellament no està inicialitzat o actiu.");
        }
        server.unPairRegisterService(journeyService);
    }

    public void broadcastStationID(StationID stationID) throws ConnectException {
        if (stationID == null) {
            throw new IllegalArgumentException("StationID no pot ser nul.");
        }
        server.registerLocation(null, stationID);
        System.out.printf("Broadcast de l'estació rebut: %s%n", stationID.getId());
    }

    public void startDriving() throws ConnectException, ProceduralException {
        if (isDriving) {
            throw new ProceduralException("El vehicle ja està en conducció.");
        }
        try {
            microController.startDriving();
            isDriving = true;
        } catch (PMVPhisicalException e) {
            throw new RuntimeException("Error físic al vehicle: " + e.getMessage(), e);
        }
    }

    public void stopDriving() throws ConnectException, ProceduralException {
        if (!isDriving) {
            throw new ProceduralException("No es pot aturar el vehicle perquè no està en conducció.");
        }
        try {
            microController.stopDriving();
            isDriving = false;
        } catch (PMVPhisicalException e) {
            throw new RuntimeException("Error físic al vehicle: " + e.getMessage(), e);
        }
    }

    public BigDecimal calculateImport(float distance, int duration, float avgSpeed, BigDecimal costPerKm, BigDecimal costPerMinute) {
        if (distance <= 0 || duration <= 0 || avgSpeed <= 0 || costPerKm.compareTo(BigDecimal.ZERO) <= 0 || costPerMinute.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Els paràmetres del càlcul han de ser positius.");
        }

        BigDecimal distanceCost = BigDecimal.valueOf(distance).multiply(costPerKm);
        BigDecimal timeCost = BigDecimal.valueOf(duration).multiply(costPerMinute);
        return distanceCost.add(timeCost);
    }

    public double[] calculateValues(GeographicPoint endLocation, LocalDateTime endTime) {
        if (endLocation == null || endTime == null) {
            throw new IllegalArgumentException("Els valors inicials i finals no poden ser nuls.");
        }

        // Càlcul de la duració en segons
        long durationInSeconds = Duration.between(journey.getStartTime(), endTime).getSeconds();
        if (durationInSeconds <= 0) {
            throw new IllegalArgumentException("La duració ha de ser positiva.");
        }

        // Càlcul de la distància en quilòmetres
        double deltaLat = endLocation.getLatitude() - journey.getStartStation().ubicacio.getLatitude();
        double deltaLon = endLocation.getLongitude() - journey.getStartStation().ubicacio.getLongitude();
        double distanceInKm = Math.sqrt(deltaLat * deltaLat + deltaLon * deltaLon) * 111; // 1 grau ≈ 111 km

        // Càlcul de la velocitat mitjana
        double durationInHours = durationInSeconds / 3600.0;
        double averageSpeed = distanceInKm / durationInHours;

        // Retornem els valors com un array
        return new double[]{distanceInKm, durationInSeconds / 60.0, averageSpeed}; // Distància (km), duració (minuts), velocitat (km/h)
    }



    public void selectPaymentMethod(char opt) throws ProceduralException {
        // Comprovar si el mètode seleccionat és vàlid
        if (opt != 'C' && opt != 'D' && opt != 'W' && opt != 'P') {
            throw new ProceduralException("Mètode de pagament no vàlid.");
        }

        // Assigna o executa accions basades en el mètode seleccionat
        switch (opt) {
            case 'C':
                System.out.println("Pagament seleccionat: Crèdit.");
                break;
            case 'D':
                System.out.println("Pagament seleccionat: Dèbit.");
                break;
            case 'W':
                System.out.println("Pagament seleccionat: Wallet.");
                break;
            case 'P':
                System.out.println("Pagament seleccionat: PayPal.");
                break;
        }

        System.out.println("Mètode de pagament processat correctament: " + opt);
    }



    // Operació interna per realitzar el pagament
    protected void realizePayment(BigDecimal imp) throws NotEnoughWalletException {//protected perque es pugui accedir desde test
        if (imp.compareTo(wallet.getBalance()) > 0) {
            throw new NotEnoughWalletException("Saldo insuficient al moneder.");
        }
        wallet.subBalance(imp);
        System.out.println("Pagament completat: " + imp + " € deduïts del moneder.");
    }

}
