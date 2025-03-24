package de.voomdoon.util.kml.geometry;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.InstanceOfAssertFactories;
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
	 * @since 0.1.0
	 */
	@Test
	void testClean_altitudeThreshold_doesNotRemoveCoordinateWithDifferentAltitude() throws Exception {
		logTestStart();

		LineString lineString = new LineString();
		lineString.addToCoordinates(1, 1, 1);
		lineString.addToCoordinates(2, 2, 2);
		lineString.addToCoordinates(2, 2, 3.1);
		lineString.addToCoordinates(3, 3, 3);

		LineStringCleaner cleaner = new LineStringCleaner();
		cleaner.setAltitudeThreshold(1);

		cleaner.clean(lineString);

		assertThat(lineString).extracting(LineString::getCoordinates).asInstanceOf(InstanceOfAssertFactories.LIST)
				.containsExactly(//
						new Coordinate(1, 1, 1), //
						new Coordinate(2, 2, 2), //
						new Coordinate(2, 2, 3.1), //
						new Coordinate(3, 3, 3));
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void testClean_altitudeThreshold_removesSimilarCoordinates() throws Exception {
		logTestStart();

		LineString lineString = new LineString();
		lineString.addToCoordinates(1, 1, 1);
		lineString.addToCoordinates(2, 2, 2);
		lineString.addToCoordinates(2, 2, 2.1);
		lineString.addToCoordinates(3, 3, 3);

		LineStringCleaner cleaner = new LineStringCleaner();
		cleaner.setAltitudeThreshold(1);

		cleaner.clean(lineString);

		assertThat(lineString).extracting(LineString::getCoordinates).asInstanceOf(InstanceOfAssertFactories.LIST)
				.containsExactly(//
						new Coordinate(1, 1, 1), //
						new Coordinate(2, 2, 2), //
						new Coordinate(3, 3, 3));
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void testClean_default_doesNotRemoveCoordinateWithSlightlyDifferentAltitude() throws Exception {
		logTestStart();

		LineString lineString = new LineString();
		lineString.addToCoordinates(1, 1, 1);
		lineString.addToCoordinates(2, 2, 2);
		lineString.addToCoordinates(2, 2, 2.1);
		lineString.addToCoordinates(3, 3, 3);

		new LineStringCleaner().clean(lineString);

		assertThat(lineString).extracting(LineString::getCoordinates).asInstanceOf(InstanceOfAssertFactories.LIST)
				.containsExactly(//
						new Coordinate(1, 1, 1), //
						new Coordinate(2, 2, 2), //
						new Coordinate(2, 2, 2.1), //
						new Coordinate(3, 3, 3));
	}

	/**
	 * @since 0.1.0
	 */
	@Test
	void testClean_default_removesEqualCoordinates() throws Exception {
		logTestStart();

		LineString lineString = new LineString();
		lineString.addToCoordinates(1, 1, 1);
		lineString.addToCoordinates(2, 2, 2);
		lineString.addToCoordinates(2, 2, 2);
		lineString.addToCoordinates(3, 3, 3);

		new LineStringCleaner().clean(lineString);

		assertThat(lineString).extracting(LineString::getCoordinates).asInstanceOf(InstanceOfAssertFactories.LIST)
				.containsExactly(//
						new Coordinate(1, 1, 1), //
						new Coordinate(2, 2, 2), //
						new Coordinate(3, 3, 3));
	}
}
