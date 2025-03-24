package de.voomdoon.util.kml.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
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
		@ParameterizedTest
		@EnumSource(WriteType.class)
		void test_closesFile(WriteType plain) throws IOException {
			logTestStart();

			// FEATURE test with plain

			if (plain == WriteType.DISABLE_NAMESPACE_PREFIXES) {
				writer.disableNamespacePrefixes();
			}

			writer.write(kml, fileName);

			boolean actual = new File(fileName).delete();

			assertThat(actual).isTrue();
		}

		/**
		 * @since 0.1.0
		 */
		@ParameterizedTest
		@EnumSource(WriteType.class)
		void test_IOException_fileLocked_messageContainsReason(WriteType plain) throws Exception {
			logTestStart();

			new File(fileName).createNewFile();

			if (plain == WriteType.DISABLE_NAMESPACE_PREFIXES) {
				writer.disableNamespacePrefixes();
			}

			try (FileChannel channel = FileChannel.open(Path.of(fileName), StandardOpenOption.APPEND)) {
				channel.lock();

				IOException actual = assertThrows(IOException.class, () -> writer.write(kml, fileName));

				assertThat(actual).hasMessageContaining(
						"The process cannot access the file because another process has locked a portion of the file");
			}
		}

		/**
		 * @since 0.1.0
		 */
		@ParameterizedTest
		@ValueSource(ints = { 1, 100000 })
		void test_plain(int featureCount) throws Exception {
			logTestStart();

			Document document = kml.createAndSetDocument();

			for (int i = 0; i < featureCount; i++) {
				document.addToFeature(getRandomFeature());
			}

			File file = new File(fileName);
			writer.disableNamespacePrefixes().write(kml, file.toString());

			String actual = Files.readString(Path.of(file.toString()), StandardCharsets.UTF_8);

			assertThat(actual).contains("xmlns=\"http://www.opengis.net/kml/2.2\"");

			assertThat(actual).contains("<Document");
			assertThat(actual).doesNotContain("kml:");
			assertThat(actual.trim()).endsWith(">");
		}

		/**
		 * @return {@link Feature}
		 * @since 0.1.0
		 * @deprecated move to somewhere
		 */
		@Deprecated
		private Feature getRandomFeature() {
			Placemark placemark = new Placemark();
			placemark.setName(Integer.toString(RAND.nextInt()));
			placemark.setDescription(Integer.toString(RAND.nextInt()));
			placemark.setGeometry(constructPoint(Math.random(), Math.random()));

			return placemark;
		}
	}

	/**
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	enum WriteType {

		/**
		 * @since 0.1.0
		 */
		DEFAULT,

		/**
		 * @since 0.1.0
		 */
		DISABLE_NAMESPACE_PREFIXES
	}
}
