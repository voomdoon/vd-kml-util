package de.voomdoon.util.kml.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.voomdoon.testing.file.TempFileExtension;
import de.voomdoon.testing.file.TempOutputFile;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * Test class for {@link KmlWriter}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class KmlWriterTest {

	/**
	 * Test class for {@link KmlWriter#write(Kml, String)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	@ExtendWith(TempFileExtension.class)
	@ExtendWith(TempFileExtension.class)
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
		private Kml kml = new Kml();

		/**
		 * @since 0.1.0
		 */
		private KmlWriter writer = new KmlWriter();

		/**
		 * @since 0.1.0
		 */
		@Test
		void test(@TempOutputFile File outputFile) throws Exception {
			logTestStart();

			writer.write(kml, outputFile.toString());

			assertThat(outputFile).isFile();
		}

		/**
		 * @throws IOException
		 * 
		 * @since 0.2.0
		 */
		@ParameterizedTest
		@EnumSource(WriteType.class)
		void test_closesFile(WriteType plain, @TempOutputFile File outputFile) throws IOException {
			logTestStart();

			// FEATURE test with plain

			if (plain == WriteType.DISABLE_NAMESPACE_PREFIXES) {
				writer.disableNamespacePrefixes();
			}

			writer.write(kml, outputFile.toString());

			boolean actual = outputFile.delete();

			assertThat(actual).isTrue();
		}

		/**
		 * @since 0.1.0
		 */
		@ParameterizedTest
		@EnumSource(WriteType.class)
		void test_IOException_fileLocked_messageContainsReason(WriteType plain, @TempOutputFile File outputFile)
				throws Exception {
			logTestStart();

			outputFile.createNewFile();

			if (plain == WriteType.DISABLE_NAMESPACE_PREFIXES) {
				writer.disableNamespacePrefixes();
			}

			try (FileChannel channel = FileChannel.open(outputFile.toPath(), StandardOpenOption.APPEND)) {
				channel.lock();

				IOException actual = assertThrows(IOException.class, () -> writer.write(kml, outputFile.toString()));

				assertThat(actual).hasMessageContaining(
						"The process cannot access the file because another process has locked a portion of the file");
			}
		}

		/**
		 * @since 0.1.0
		 */
		@ParameterizedTest
		@ValueSource(ints = { 1, 100000 })
		void test_plain(int featureCount, @TempOutputFile File outputFile) throws Exception {
			logTestStart();

			Document document = kml.createAndSetDocument();

			for (int i = 0; i < featureCount; i++) {
				document.addToFeature(getRandomFeature());
			}

			writer.disableNamespacePrefixes().write(kml, outputFile.toString());

			String actual = Files.readString(outputFile.toPath(), StandardCharsets.UTF_8);

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
