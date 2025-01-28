package services.smartfeatures;

import data.VehicleID;
import exceptions.CorruptedImgException;

import java.awt.image.BufferedImage;

/**
 * Interfície que defineix la funcionalitat per descodificar codis QR associats a vehicles.
 *
 * Relació:
 * - Implementada pel  `QRDecoderImp`, que retorna un VehicleID fix per a proves.
 * - Provada a `QRDecoderTest`, que valida escenaris de QR vàlids i corruptes.
 */
public interface QRDecoder {
    /**
     * Obté l'identificador del vehicle a partir d'una imatge QR.
     * @param QRImg La imatge QR a descodificar.
     * @return Un objecte VehicleID que identifica el vehicle.
     * @throws CorruptedImgException si la imatge QR és nul·la o no es pot descodificar.
     */
    VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException;
}
