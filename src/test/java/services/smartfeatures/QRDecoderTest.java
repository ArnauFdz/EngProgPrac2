package services.smartfeatures;

import org.junit.jupiter.api.Test;
import services.smartfeatures.QRDecoderImpl;
import exceptions.CorruptedImgException;
import data.VehicleID;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per a la classe QRDecoderImpl.
 */
public class QRDecoderTest {

    /**
     * Testa que el mètode getVehicleID retorna un VehicleID vàlid quan es proporciona una imatge QR vàlida.
     */
    @Test
    public void testGetVehicleIDValidQR() throws CorruptedImgException {
        // Simulant una imatge de QR vàlida (en aquest cas no es carrega una imatge real)
        BufferedImage qrImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); // Simulació de la imatge
        QRDecoderImpl decoder = new QRDecoderImpl();

        // Obtenim l'ID del vehicle
        VehicleID vehicleID = decoder.getVehicleID(qrImage);

        // Comprovem que el VehicleID no sigui null i que l'ID sigui el correcte
        assertNotNull(vehicleID);
        assertEquals("VH123456", vehicleID.getId()); // El VehicleID simulat que retornem
    }

    /**
     * Testa que el mètode getVehicleID llança una excepció si la imatge del QR és corrupta.
     */
    @Test
    public void testGetVehicleIDCorruptedImage() {
        // Simulant una imatge corrupta (null)
        BufferedImage corruptedImage = null;
        QRDecoderImpl decoder = new QRDecoderImpl();

        // Verifiquem que l'error CorruptedImgException es llança
        assertThrows(CorruptedImgException.class, () -> {
            decoder.getVehicleID(corruptedImage);
        });
    }

    /**
     * Testa que el mètode getVehicleID llança una excepció si la imatge del QR és null.
     */
    @Test
    public void testGetVehicleIDNullImage() {
        // Simulant una imatge null (cas on no es proporciona imatge)
        BufferedImage nullImage = null;
        QRDecoderImpl decoder = new QRDecoderImpl();

        // Verifiquem que l'error CorruptedImgException es llança
        assertThrows(CorruptedImgException.class, () -> {
            decoder.getVehicleID(nullImage);
        });
    }
}

