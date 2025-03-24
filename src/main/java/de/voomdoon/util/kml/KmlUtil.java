package de.voomdoon.util.kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.voomdoon.util.kml.io.KmlWriter;
import lombok.experimental.UtilityClass;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
@UtilityClass
public class KmlUtil {

	/**
	 * DOCME add JavaDoc for method readKml
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @since 0.1.0
	 */
	public static Kml readKml(String fileName) throws IOException {
		File file = new File(fileName);

		if (!file.exists()) {
			throw new FileNotFoundException("Failed to read KML: File not found: " + fileName);
		} else if (file.isDirectory()) {
			throw new IllegalArgumentException("Failed to read KML: Cannot read directory: " + fileName);
		}

		Kml result = Kml.unmarshal(file);

		if (result == null) {
			// TESTME FEATURE apply same monitoring as during writing (detect SAXParseException)
			throw new IOException("Unexpected error during unmarshalling KML!");
		}

		return result;
	}

	/**
	 * DOCME add JavaDoc for method writeKml
	 * 
	 * @param kml
	 * @param fileName
	 * @throws IOException
	 * @since 0.1.0
	 */
	public static void writeKml(Kml kml, String fileName) throws IOException {
		new KmlWriter().write(kml, fileName);
	}
}
