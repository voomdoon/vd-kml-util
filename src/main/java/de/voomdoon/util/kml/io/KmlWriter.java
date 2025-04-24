package de.voomdoon.util.kml.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * Writes {@link Kml} to a file.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class KmlWriter {

	/**
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private class NormalWriter extends Writer {

		/**
		 * @since 0.1.0
		 */
		protected Writer wrapped;

		/**
		 * @since 0.1.0
		 */
		private IOException error;

		/**
		 * @param wrapped
		 *            {@link Writer}
		 * @since 0.1.0
		 */
		public NormalWriter(Writer wrapped) {
			this.wrapped = wrapped;
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		public void close() throws IOException {
			wrapped.close();
		}

		/**
		 * @since 0.1.0
		 */
		@Override
		public void flush() throws IOException {
			try {
				wrapped.flush();
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
			wrapped.write(cbuf, off, len);
		}
	}

	/**
	 * Writes plain XML without namespace prefixes.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private class PlainWriter extends NormalWriter {

		/**
		 * @since 0.1.0
		 */
		private String temp = null;

		/**
		 * @param wrapped
		 * @since 0.1.0
		 */
		public PlainWriter(Writer wrapped) {
			super(wrapped);
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
				// FIXME do not replace data
				body = body.replace("<kml:", "<").replace("</kml:", "</");
				wrapped.write(body.toCharArray());
			}
		}
	}

	/**
	 * @since 0.1.0
	 */
	private boolean plainXml;

	/**
	 * Causes writing of plain XML without namespace prefixes.
	 * 
	 * @return this {@link KmlWriter}
	 * @since 0.1.0
	 */
	public KmlWriter disableNamespacePrefixes() {
		plainXml = true;

		return this;
	}

	/**
	 * Writes {@link Kml} to a {@link File}.
	 * 
	 * @param kml
	 *            {@link Kml} to write
	 * @param fileName
	 *            file name to write to
	 * @throws IOException
	 * @since 0.1.0
	 */
	public void write(Kml kml, String fileName) throws IOException {
		try (NormalWriter writer = getWriter(new BufferedWriter(new FileWriter(fileName)))) {
			marshal(kml, writer);
		}
	}

	/**
	 * @param writer
	 *            {@link BufferedWriter}
	 * @return {@link NormalWriter}
	 * @since 0.1.0
	 */
	private NormalWriter getWriter(BufferedWriter writer) {
		if (plainXml) {
			return new PlainWriter(writer);
		} else {
			return new NormalWriter(writer);
		}
	}

	/**
	 * Handles {@link IOException} during writing.
	 * 
	 * @param writer
	 *            {@link NormalWriter}
	 * @throws IOException
	 * @since 0.1.0
	 */
	private void handleIOException(NormalWriter writer) throws IOException {
		if (writer.error != null) {
			throw writer.error;
		}

		throw new IOException("Unexpected error during writing KML!");// TESTME
	}

	/**
	 * @param kml
	 *            {@link Kml}
	 * @param writer
	 *            {@link NormalWriter}
	 * @throws IOException
	 * @since 0.1.0
	 */
	private void marshal(Kml kml, NormalWriter writer) throws IOException {
		boolean success = kml.marshal(writer);

		if (!success) {
			handleIOException(writer);
		}
	}
}
