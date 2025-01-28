// Paquete: data
package data;

/**
 * la clase GeographicPoint representa una localització geogràfica.
 */
public final class GeographicPoint {
    private final float latitude;
    private final float longitude;

    public GeographicPoint(float latitude, float longitude) {
        if (latitude < -90.0f || latitude > 90.0f) {
            throw new IllegalArgumentException("La latitud ha d'estar entre -90 y 90.");
        }
        if (longitude < -180.0f || longitude > 180.0f) {
            throw new IllegalArgumentException("La longitud ha d'estar entre -180 y 180.");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeographicPoint gP = (GeographicPoint) o;
        return ((latitude == gP.latitude) && (longitude == gP.longitude));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(latitude);
        result = prime * result + Float.floatToIntBits(longitude);
        return result;
    }

    @Override
    public String toString() {
        return "Geographic point {latitude='" + latitude + "', longitude='" + longitude + "'}";
    }

}
