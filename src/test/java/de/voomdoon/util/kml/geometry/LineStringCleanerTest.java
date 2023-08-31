package de.voomdoon.util.kml.geometry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class LineStringCleanerTest extends LoggingCheckingTestBase {

	/**
	 * DOCME add JavaDoc for method testClean_default
	 * 
	 * @since 0.1.0
	 */
	@Test
	void testClean_default_removedEqualCoordinates() throws Exception {
		logTestStart();

		LineString lineString = new LineString();
		lineString.addToCoordinates(1, 1, 1);
		lineString.addToCoordinates(2, 2, 2);
		lineString.addToCoordinates(2, 2, 2);
		lineString.addToCoordinates(3, 3, 3);

		new LineStringCleaner().clean(lineString);

		assertThat(lineString).extracting(LineString::getCoordinates).asList().containsExactly(//
				new Coordinate(1, 1, 1), //
				new Coordinate(2, 2, 2), //
				new Coordinate(3, 3, 3));
	}

}
