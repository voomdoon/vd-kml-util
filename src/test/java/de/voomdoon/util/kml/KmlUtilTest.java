package de.voomdoon.util.kml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	static class ReadKmlTest extends LoggingCheckingTestBase {

		/**
		 * DOCME add JavaDoc for method test
		 * 
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
		 * DOCME add JavaDoc for method test
		 * 
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
}
