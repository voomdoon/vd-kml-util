package de.voomdoon.util.kml.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class KmlWriterTest {

	/**
	 * DOCME add JavaDoc for KmlWriterTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class WriteTest extends LoggingCheckingTestBase {

		/**
		 * @since 0.1.0
		 */
		private static final Random RAND = new Random();

		/**
		 * Constructs a {@link Point}.
		 * 
		 * @param longitude
		 * @param latitude
		 * @return {@link Point}
		 * @since 0.1.0
		 */
		public static Point constructPoint(double longitude, double latitude) {
			Point point = new Point();
			point.addToCoordinates(longitude, latitude);

			return point;
		}

		/**
		 * @since 0.1.0
		 */
		private String fileName;

		/**
		 * @since 0.1.0
		 */
		private Kml kml = new Kml();

		/**
		 * @since 0.1.0
		 */
		private KmlWriter writer = new KmlWriter();

		/**
		 * DOCME add JavaDoc for constructor KmlWriterTest.WriteTest
		 * 
		 * @throws IOException
		 * @since 0.1.0
		 */
		public WriteTest() throws IOException {
			fileName = getTempDirectory() + "/file.kml";
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test() throws Exception {
			logTestStart();

			writer.write(kml, fileName);

			assertThat(new File(fileName)).isFile();
		}

		/**
		 * @throws IOException
		 * 
		 * @since 0.2.0
		 */
		@Test
		void test_closesFile() throws IOException {
			logTestStart();

			writer.write(kml, fileName);

			boolean actual = new File(fileName).delete();

			assertThat(actual).isTrue();
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_IOException_fileLocked_messageContainsReason() throws Exception {
			logTestStart();

			new File(fileName).createNewFile();

			try (FileChannel channel = FileChannel.open(Path.of(fileName), StandardOpenOption.APPEND)) {
				channel.lock();

				IOException actual = assertThrows(IOException.class, () -> writer.write(kml, fileName));

				assertThat(actual).hasMessageContaining(
						"The process cannot access the file because another process has locked a portion of the file");
			}
		}
	}
}
