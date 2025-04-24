package de.voomdoon.util.kml.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * Reads {@link Kml} from {@link File}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class KmlReader {

	/**
	 * Reads {@link Kml} from {@link File}.
	 * 
	 * @param fileName
	 *            name of the {@link File} to read from
	 * @return {@link Kml}
	 * @throws IOException
	 * @since 0.1.0
	 */
	public Kml read(String fileName) throws IOException {
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
}
