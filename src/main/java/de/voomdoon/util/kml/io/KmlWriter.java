package de.voomdoon.util.kml.io;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class KmlWriter {

	/**
	 * 
	 * DOCME add JavaDoc for KmlWriter
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private class MyWriter extends Writer {

		/**
		 * @since 0.1.0
		 */
		private IOException error;

		/**
		 * @since 0.1.0
		 */
		private FileWriter fileWriter;

		/**
		 * @since 0.1.0
		 */
		private String temp = null;

		/**
		 * DOCME add JavaDoc for constructor MyWriter
		 * 
		 * @param fileWriter
		 * @since 0.1.0
		 */
		public MyWriter(FileWriter fileWriter) {
			this.fileWriter = fileWriter;
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		public void close() throws IOException {
			fileWriter.close();
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		public void flush() throws IOException {
			try {
				fileWriter.flush();
			} catch (IOException e) {
				error = e;
				throw e;
			}
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			char[] input = new char[len];
			System.arraycopy(cbuf, off, input, 0, len);
			String string = new String(input);

			String body;

			if (temp != null) {
				body = temp + string;
				temp = null;
			} else {
				body = string;
			}

			body = body.replace("xmlns:kml=\"http://www.opengis.net/kml/2.2\"",
					"xmlns=\"http://www.opengis.net/kml/2.2\"");

			if (!body.endsWith(">\n")) {
				temp = body;
			} else {
				body = body.replace("kml:", "");

				try {
					fileWriter.write(body.toCharArray());
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}

	/**
	 * Wrapper to catch any {@link IOException} during writing.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private static class OutputStreamWrapper extends OutputStream {

		/**
		 * @since 0.1.0
		 */
		private IOException error;

		/**
		 * @since 0.1.0
		 */
		private OutputStream wrapped;

		/**
		 * DOCME add JavaDoc for constructor OutputStreamWrapper
		 * 
		 * @param wrapped
		 * @since 0.1.0
		 */
		public OutputStreamWrapper(OutputStream wrapped) {
			super();
			this.wrapped = wrapped;
		}

		/**
		 * @since 0.1.0
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

	private boolean plainXml;

	/**
	 * DOCME add JavaDoc for method withPlainXml
	 * 
	 * @return this {@link KmlWriter}
	 * @since 0.1.0
	 */
	public KmlWriter withPlainXml() {
		plainXml = true;

		return this;
	}

	/**
	 * DOCME add JavaDoc for method write
	 * 
	 * @param kml
	 * @param fileName
	 * @throws IOException
	 * @since 0.1.0
	 */
	public void write(Kml kml, String fileName) throws IOException {
		if (plainXml) {
			writePlainXml(kml, fileName);
		} else {
			try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
				marshal(kml, outputStream);
			}
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
	private void marshal(Kml kml, OutputStream outputStream) throws IOException {
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

	/**
	 * DOCME add JavaDoc for method writePlainXml
	 * 
	 * @param kml
	 * @param fileName
	 * @throws IOException
	 * @since 0.1.0
	 */
	private void writePlainXml(Kml kml, String fileName) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName);

		// FIXME file is truncated sometimes (big file?)
		MyWriter writer = new MyWriter(fileWriter);

		// FIXME retry on IOException: An unexpected network error occurred
		boolean success = kml.marshal(writer);

		writer.close();

		if (!success) {
			if (writer.error != null) {
				throw writer.error;
			}

			throw new IOException("Failed to write KML!");
		}
	}
}
