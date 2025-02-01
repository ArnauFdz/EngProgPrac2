package micromobility;

import data.*;
import micromobility.payment.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JourneyService {

    // Membres de la classe
    private  ServiceID serviceID; // Identificador únic del servei
    private final UserAccount user; // Usuari que realitza el viatge
    private final VehicleID vehicle; // Vehicle utilitzat
    private StationID startStation; // Estació d'inici
    private StationID endStation; // Estació de finalització
    private LocalDateTime startTime; // Hora d'inici
    private LocalDateTime endTime; // Hora de finalització
    private boolean active; // Estat del servei (actiu o finalitzat)

    //Variables que falten en comptes de caslcular-les a posteriori
    private double distance;
    private float averageSpeed;
    private int duration;// in minutes
    private BigDecimal cost;
    private GeographicPoint startLocation;
    private GeographicPoint endLocation;
    private Payment payment;

    // Constructor
    public JourneyService(UserAccount user, VehicleID vehicle) {
        if (user == null || vehicle == null) {
            throw new IllegalArgumentException("Els paràmetres no poden ser nuls.");
        }

        this.user = user;
        this.vehicle = vehicle;
        this.active = false; // Per defecte, no actiu fins que s'inicia
    }

    // Getters
    public ServiceID getServiceID() {
        return serviceID;
    }

    public UserAccount getUser() {
        return user;
    }

    public VehicleID getVehicle() {
        return vehicle;
    }

    public StationID getStartStation() {
        return startStation;
    }

    public StationID getEndStation() {
        return endStation;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isActive() {
        return active;
    }

    //getters adicionals


    public double getDistance() {
        return distance;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public BigDecimal getCost() {
        return cost;
    }
    public int getDuration(){
        return duration;
    }

    public GeographicPoint getEndLocation() {
        return endLocation;
    }

    public GeographicPoint getStartLocation() {
        return startLocation;
    }

    public Payment getPayment() {
        return payment;
    }

    // Setters
    public void setEndStation(StationID endStation) {
        if (endStation == null) {
            throw new IllegalArgumentException("L'estació de finalització no pot ser nul·la.");
        }
        this.endStation = endStation;
    }

    public boolean siActive(){return active;}


    public void setServiceInit(StationID startStation, GeographicPoint startLocation) {
        if (active) {
            throw new IllegalStateException("El servei ja està actiu.");
        }
        this.startTime = LocalDateTime.now();
        this.startStation = startStation;
        this.startLocation = startLocation;
        this.active = true;
        System.out.println("Servei iniciat a: " + startTime);
    }

    public void setServiceFinish(StationID endStation, GeographicPoint endLocation,
                                 LocalDateTime endTime, double distance, float avgSpeed,
                                 int duration, BigDecimal cost) {
        if (!active) {
            throw new IllegalStateException("El servei no està actiu i no es pot finalitzar.");
        }

        this.endStation = endStation;
        this.endLocation = endLocation;
        this.endTime = endTime;
        this.distance = distance;
        this.averageSpeed = avgSpeed;
        this.duration = duration;
        this.cost = cost;
        this.active = false;
        System.out.println("Servei finalitzat a: " + endTime);
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setServiceID(ServiceID serviceID) {
        this.serviceID = serviceID;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAverageSpeed(float avgSpeed) {
        this.averageSpeed = avgSpeed;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
