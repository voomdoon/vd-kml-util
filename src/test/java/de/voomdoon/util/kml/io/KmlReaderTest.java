package de.voomdoon.util.kml.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.voomdoon.testing.file.TempFileExtension;
import de.voomdoon.testing.file.TempInputDirectory;
import de.voomdoon.testing.file.TempInputFile;
import de.voomdoon.testing.file.WithTempInputFiles;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * Test class for {@link KmlReader}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class KmlReaderTest {

	/**
	 * Test class for {@link KmlReader#read(String)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	@ExtendWith(TempFileExtension.class)
	@WithTempInputFiles(extension = "kml")
	class ReadTest extends LoggingCheckingTestBase {

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_directory_error(@TempInputDirectory File inputDirectory) throws Exception {
			logTestStart();

			inputDirectory.mkdirs();

			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
					() -> new KmlReader().read(inputDirectory.toString()));

			assertThat(exception).hasMessageContaining("directory");
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_file(@TempInputFile Path inputFile) throws IOException {
			logTestStart();

			Files.copy(getClass().getResourceAsStream("/kml/Document.kml"), inputFile);

			Kml kml = new KmlReader().read(inputFile.toString());

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
