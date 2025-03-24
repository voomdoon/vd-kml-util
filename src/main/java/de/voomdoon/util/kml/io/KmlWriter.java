package de.voomdoon.util.kml.io;

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
		protected FileWriter fileWriter;

		/**
		 * @since 0.1.0
		 */
		IOException error;

		/**
		 * DOCME add JavaDoc for constructor MyWriter
		 * 
		 * @param fileWriter
		 * @since 0.1.0
		 */
		public NormalWriter(FileWriter fileWriter) {
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
			fileWriter.write(cbuf, off, len);
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
		 * @param fileWriter
		 * @since 0.1.0
		 */
		public PlainWriter(FileWriter fileWriter) {
			super(fileWriter);
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
	 * @since 0.1.0
	 */
	private boolean plainXml;

	/**
	 * Causes writing of plain XML without namespace prefixes.
	 * 
	 * @return this {@link KmlWriterV5_refactor_renameMethod}
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
		try (NormalWriter writer = getWriter(new FileWriter(fileName))) {
			marshal(kml, writer);
		}
	}

	/**
	 * DOCME add JavaDoc for method getWriter
	 * 
	 * @param fileWriter
	 * @return
	 * @since 0.1.0
	 */
	private NormalWriter getWriter(FileWriter fileWriter) {
		if (plainXml) {
			return new PlainWriter(fileWriter);
		} else {
			return new NormalWriter(fileWriter);
		}
	}

	/**
	 * DOCME add JavaDoc for method marshal
	 * 
	 * @param kml
	 * @param fileName
	 * @throws IOException
	 * @since 0.1.0
	 */
	private void handleIOException(NormalWriter writer) throws IOException {
		if (writer.error != null) {
			throw writer.error;
		}

		throw new IOException("Failed to write KML!");
	}

	/**
	 * DOCME add JavaDoc for method writePlainXml
	 * 
	 * @param kml
	 * @param fileName
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
