package de.voomdoon.util.kml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class KmlUtilTest {

	/**
	 * DOCME add JavaDoc for KmlUtilTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class ReadKmlTest extends LoggingCheckingTestBase {

		/**
		 * @throws IOException
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_File() throws IOException {
			logTestStart();

			Path path = Paths.get(getTempDirectory().toString(), "input.kml");
			Files.copy(getClass().getResourceAsStream("/kml/Document.kml"), path);

			Kml kml = KmlUtil.readKml(path.toString());

			assertThat(kml).isNotNull();
		}

		/**
		 * @throws IOException
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_File_notFound() throws IOException {
			logTestStart();

			assertThrows(FileNotFoundException.class, () -> KmlUtil.readKml("test.kml"));
		}
	}

	/**
	 * DOCME add JavaDoc for KmlUtilTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class WriteKmlTest extends LoggingCheckingTestBase {

		/**
		 * DOCME add JavaDoc for method test
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test() throws Exception {
			logTestStart();

			Kml kml = new Kml();

			String outputFileName = getTempDirectory() + "/file.kml";
			KmlUtil.writeKml(kml, outputFileName);

			assertThat(new File(outputFileName)).isFile();
		}

		/**
		 * DOCME add JavaDoc for method test_IOException
		 * 
		 * @since DOCME add inception version number
		 */
		@Test
		void test_IOException_fileLocked_messageContainsReason() throws Exception {
			logTestStart();

			Kml kml = new Kml();

			String outputFileName = getTempDirectory() + "/file.kml";

			new File(outputFileName).createNewFile();

			try (FileChannel channel = FileChannel.open(Path.of(outputFileName), StandardOpenOption.APPEND)) {
				channel.lock();
				IOException actual = assertThrows(IOException.class, () -> KmlUtil.writeKml(kml, outputFileName));

				assertThat(actual).hasMessageContaining(
						"The process cannot access the file because another process has locked a portion of the file");
			}
		}
	}
}
