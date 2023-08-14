package de.voomdoon.util.kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.micromata.opengis.kml.v_2_2_0.Kml;
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
		}

		return Kml.unmarshal(file);
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
		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			marshal(kml, outputStream);
		}
	}

	/**
	 * DOCME add JavaDoc for method marshal
	 * 
	 * @param kml
	 * @param outputStream
	 * @throws IOException
	 * @since 0.1.0
	 */
	private static void marshal(Kml kml, FileOutputStream outputStream) throws IOException {
		// marshal is caching and printing any JAXBException
		boolean success = kml.marshal(outputStream);

		if (!success) {
			throw new IOException("Failed to write KML!");
		}
	}
}
