package services.smartfeatures;

import data.VehicleID;
import exceptions.CorruptedImgException;
import java.awt.image.BufferedImage;

/**
 * Implementació de QRDecoder per simular la decodificació de codis QR.
 */
public class QRDecoderImpl implements QRDecoder {

    @Override
    public VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException {
        // Simulació: Si la imatge és null o si no es pot "decodificar", llançar una excepció.
        if (QRImg == null) {
            throw new CorruptedImgException("Imatge no vàlida o corrupta.");
        }

        // Simulació: Si el QR és vàlid, retornem un VehicleID fictici
        return new VehicleID("VH123456"); // Simulant que el codi QR conté aquest ID de vehicle
    }
}
