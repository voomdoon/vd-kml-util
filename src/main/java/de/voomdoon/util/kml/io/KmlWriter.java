package de.voomdoon.util.kml.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
	private class NormalWriter extends Writer {

		/**
		 * @since 0.1.0
		 */
		protected Writer wrapped;

		/**
		 * @since 0.1.0
		 */
		IOException error;

		/**
		 * DOCME add JavaDoc for constructor MyWriter
		 * 
		 * @param wrapped
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
	 * 
	 * DOCME add JavaDoc for KmlWriter
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
		 * DOCME add JavaDoc for constructor MyWriter
		 * 
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

			// XXX will not work if the namespace is distributed across two buffers? (or done by temp?)
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
	 * DOCME add JavaDoc for method write
	 * 
	 * @param kml
	 * @param fileName
	 * @throws IOException
	 * @since 0.1.0
	 */
	public void write(Kml kml, String fileName) throws IOException {
		try (NormalWriter writer = getWriter(new BufferedWriter(new FileWriter(fileName)))) {
			marshal(kml, writer);
		}
	}

	/**
	 * DOCME add JavaDoc for method getWriter
	 * 
	 * @param writer
	 * @return
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
	 * DOCME add JavaDoc for method handleIOException
	 * 
	 * @param writer
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
	 * DOCME add JavaDoc for method marshal
	 * 
	 * @param kml
	 * @param writer
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
