package de.voomdoon.util.kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
	 * DOCME add JavaDoc for KmlUtil
	 *
	 * @author André Schulz
	 *
	 * @since DOCME add inception version number
	 */
	private static class OutputStreamWrapper extends OutputStream {

		/**
		 * @since DOCME add inception version number
		 */
		private IOException error;

		/**
		 * @since DOCME add inception version number
		 */
		private OutputStream wrapped;

		/**
		 * DOCME add JavaDoc for constructor OutputStreamWrapper
		 * 
		 * @param wrapped
		 * @since DOCME add inception version number
		 */
		public OutputStreamWrapper(OutputStream wrapped) {
			super();
			this.wrapped = wrapped;
		}

		/**
		 * @since DOCME add inception version number
		 */
		@Override
		public void write(int b) throws IOException {
			try {
				wrapped.write(b);
			} catch (IOException e) {
				error = e;
				throw e;
			}
		}
	}

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
	private static void marshal(Kml kml, OutputStream outputStream) throws IOException {
		// OPTIMIZE speed: maybe write in cache and write in file at once (if writing to network)

		OutputStreamWrapper wrapper = new OutputStreamWrapper(outputStream);

		// marshal is caching and printing any JAXBException
		// => use wrapper for OutputStream to catch any IOException
		boolean success = kml.marshal(wrapper);

		if (!success) {
			if (wrapper.error != null) {
				throw wrapper.error;
			}

			throw new IOException("Failed to write KML!");
		}
	}
}
