package de.voomdoon.util.kml.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
class KmlReaderTest {

	/**
	 * DOCME add JavaDoc for KmlReaderTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class ReadTest extends LoggingCheckingTestBase {

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_directory_error() throws Exception {
			logTestStart();

			String directory = getTempDirectory().toString();

			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> new KmlReader().read(directory));

			assertThat(exception).hasMessageContaining("directory");
		}

		/**
		 * @throws IOException
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_file() throws IOException {
			logTestStart();

			Path path = Paths.get(getTempDirectory().toString(), "input.kml");
			Files.copy(getClass().getResourceAsStream("/kml/Document.kml"), path);

			Kml kml = new KmlReader().read(path.toString());

			assertThat(kml).isNotNull();
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_FileNotFoundException_file_notFound() {
			logTestStart();

			assertThrows(FileNotFoundException.class, () -> new KmlReader().read("test.kml"));
		}
	}
}
