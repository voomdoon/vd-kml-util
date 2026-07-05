package de.voomdoon.util.kml;

import java.io.File;
import java.io.IOException;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.voomdoon.util.kml.io.KmlReader;
import de.voomdoon.util.kml.io.KmlWriter;

/**
 * Utility for {@link Kml}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public final class KmlUtil {

	/**
	 * Prevents instantiation.
	 */
	private KmlUtil() {
	}

	/**
	 * Reads {@link Kml} from {@link File}.
	 * 
	 * @param fileName
	 *            name of the {@link File} to read from
	 * @return {@link Kml}
	 * @throws IOException
	 *             if the {@link File} cannot be read
	 * @since 0.1.0
	 */
	public static Kml readKml(String fileName) throws IOException {
		return new KmlReader().read(fileName);
	}

	/**
	 * Writes {@link Kml} to a {@link File}.
	 * 
	 * @param kml
	 *            {@link Kml} to write
	 * @param fileName
	 *            file name as {@link String}
	 * @throws IOException
	 *             if the {@link File} cannot be written
	 * @since 0.1.0
	 */
	public static void writeKml(Kml kml, String fileName) throws IOException {
		new KmlWriter().write(kml, fileName);
	}
}
