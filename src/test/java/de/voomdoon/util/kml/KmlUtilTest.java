package de.voomdoon.util.kml;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.voomdoon.testing.file.TempFileExtension;
import de.voomdoon.testing.file.TempInputFile;
import de.voomdoon.testing.file.TempOutputFile;
import de.voomdoon.testing.file.WithTempInputFiles;
import de.voomdoon.testing.file.WithTempOutputFiles;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
@ExtendWith(TempFileExtension.class)
@WithTempInputFiles(extension = "kml")
@WithTempOutputFiles(extension = "kml")
class KmlUtilTest extends LoggingCheckingTestBase {

	/**
	 * @throws IOException
	 * 
	 * @since 0.1.0
	 */
	@Test
	void testReadKml(@TempInputFile Path inputFile) throws IOException {
		logTestStart();

		Files.copy(getClass().getResourceAsStream("/kml/Document.kml"), inputFile);

		Kml kml = KmlUtil.readKml(inputFile.toString());

		assertThat(kml).isNotNull();
	}

	/**
	 * DOCME add JavaDoc for method test
	 * 
	 * @since 0.1.0
	 */
	@Test
	void testWriteKml(@TempOutputFile File outputFile) throws Exception {
		logTestStart();

		Kml kml = new Kml();

		KmlUtil.writeKml(kml, outputFile.toString());

		assertThat(outputFile).isFile();
	}
}
