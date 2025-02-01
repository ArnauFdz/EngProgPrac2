package micromobility;

import data.*;
import exceptions.*;
import services.*;
import micromobility.payment.*;
import services.smartfeatures.ArduinoMicroController;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import javax.xml.crypto.Data;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Classe que gestiona la lògica d'un viatge utilitzant un vehicle PMV.
 */
public class JourneyRealizeHandler {
    private Server server;
    private QRDecoder qrDecoder;
    private ArduinoMicroController microController;
    private Wallet wallet;
    private JourneyService journey;
    private UnbondedBTSignal btSignal;
    private PMVehicle veh;
    private StationID st;
    private UserAccount user;

    public JourneyRealizeHandler(Server server, QRDecoder qrDecoder, ArduinoMicroController microController, UnbondedBTSignal btSignal) {
        if (server == null || qrDecoder == null || microController == null || btSignal == null) {
            throw new IllegalArgumentException("Les dependències no poden ser nul·les.");
        }
        this.server = server;
        this.qrDecoder = qrDecoder;
        this.microController = microController;
        this.btSignal = btSignal;
    }

    //Setters

    public void setWallet(Wallet wallet){
        this.wallet = wallet;
    }

    public void setServer(Server server){
        this.server = server;
    }

    public void setQrDecoder(QRDecoder qrDecoder){
        this.qrDecoder = qrDecoder;
    }

    public void setBtSignal(UnbondedBTSignal btSignal){
        this.btSignal = btSignal;
    }

    public void setMicroController(ArduinoMicroController microController){
        this.microController = microController;
    }

    //Getters

    public Server getServer() {
        return server;
    }

    public QRDecoder getQrDecoder() {
        return qrDecoder;
    }

    public ArduinoMicroController getMicroController() {
        return microController;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public JourneyService getJourney() {
        return journey;
    }

    public UnbondedBTSignal getBtSignal() {
        return btSignal;
    }

    public PMVehicle getVeh() {
        return veh;
    }

    public StationID getSt() {
        return st;
    }

    public UserAccount getUser() {
        return user;
    }


    public void scanQR(BufferedImage qrImage)
            throws ConnectException, InvalidPairingArgsException, CorruptedImgException, PMVNotAvailException, ProceduralException {
        if (qrImage == null) {
            throw new ProceduralException("La imatge QR no pot ser nul·la.");
        }

        GeographicPoint loc = new GeographicPoint(1f, 1f);
        VehicleID vehicleID = qrDecoder.getVehicleID(qrImage);
        if(PMVState.Available.name().equals(server.checkPMVAvail(vehicleID))){
            throw new PMVNotAvailException("Vehicle no disponible");
        }

        journey = new JourneyService(user, vehicleID);
        journey.setServiceInit(st, loc);

        veh = new PMVehicle(vehicleID, loc, st, user);
        veh.setNotAvailb();
        server.registerLocation(vehicleID, st);
        server.registerPairing(user,vehicleID,st,loc,journey.getStartTime());
        microController.setBTconnection(this);
    }

    public void unPairVehicle(JourneyService journeyService)
            throws ConnectException, InvalidPairingArgsException, PairingNotFoundException, ProceduralException {
        if (journeyService == null || !journeyService.isActive()) {
            throw new ProceduralException("El servei d'aparellament no està inicialitzat o actiu.");
        }
        GeographicPoint endLoc = new GeographicPoint(10f, 10f);
        LocalDateTime endDat = LocalDateTime.now();
        journey.setServiceFinish(st, endLoc, endDat,100.0,3.5f, 40, new BigDecimal("3.7"));
        calculateValues(endLoc, endDat);
        calculateImport(journey.getDistance(), journey.getDuration(), journey.getAverageSpeed(), new BigDecimal("2.1"),new BigDecimal("2.2"));
        server.stopPairing(user, journey.getVehicle(),journey.getEndStation(),endLoc,endDat,journey.getAverageSpeed(),journey.getDistance(),journey.getDuration(),journey.getCost());
        journey.setActive(false);
        veh.setAvailb();
        veh.setLocation(endLoc);
        veh.setStationID(st);
        veh.setUser(null);
        server.registerLocation(veh.getVehicleID(), st);
        microController.undoBTconnection();
    }

    public void broadcastStationID(StationID stationID) throws ConnectException {
        if (stationID == null) {
            throw new ConnectException("StationID no pot ser nul.");
        }
        if (st == null) {
            st = stationID;
        } else if (journey != null) {
            journey.setEndStation(stationID);
            st = stationID;
        }
    }

    public void startDriving() throws ConnectException, ProceduralException {
        if (journey == null) {
            throw new ProceduralException("No hi ha trajecte associat");
        }
        if(veh == null){
            throw new ProceduralException("No hi ha vehicle associat");
        }
        veh.setUnderWay();
        journey.setActive(true);
    }

    public void stopDriving() throws ConnectException, ProceduralException {
        if (journey == null || !journey.isActive()) {
            throw new ProceduralException("No es pot aturar el vehicle perquè no està en conducció.");
        }
        try {
            microController.stopDriving();
        } catch (PMVPhisicalException e) {
            throw new RuntimeException("Error físic al vehicle: " + e.getMessage(), e);
        }
    }

    public BigDecimal calculateImport(BigDecimal costPerKm, BigDecimal costPerMinute) {
        if ( costPerKm.compareTo(BigDecimal.ZERO) <= 0 || costPerMinute.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Els paràmetres del càlcul han de ser positius.");
        }

        BigDecimal costDis = BigDecimal.valueOf(journey.getDistance()).multiply(costPerKm);
        BigDecimal costTem = BigDecimal.valueOf(journey.getDuration()).multiply(costPerMinute);



        return (costDis.add(costTem));
    }

    public void selectPaymentMethod(char opt) throws ProceduralException, NotEnoughWalletException, ConnectException {
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
                WalletPayment walletPayment = new WalletPayment(journey, journey.getCost());
                walletPayment.executePayment();
                journey.setPayment(walletPayment);
                server.registerPayment(journey.getServiceID(),user,journey.getCost(),opt);
                break;
            case 'P':
                System.out.println("Pagament seleccionat: PayPal.");
                break;
        }

        System.out.println("Mètode de pagament processat correctament: " + opt);
    }
}
