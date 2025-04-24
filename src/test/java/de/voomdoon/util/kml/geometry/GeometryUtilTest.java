package de.voomdoon.util.kml.geometry;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * Test class for {@link GeometryUtil}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class GeometryUtilTest {

	/**
	 * Test class for {@link GeometryUtil#concatenateLineStrings(MultiGeometry)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class ConcatenateLineStringsTest extends LoggingCheckingTestBase {

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_LineString1() {
			logTestStart();

			LineString lineString = new LineString();
			lineString.addToCoordinates(1.1, 1.2);
			lineString.addToCoordinates(2.1, 2.2);

			MultiGeometry multiGeometry = new MultiGeometry();
			multiGeometry.addToGeometry(lineString);

			LineString actual = GeometryUtil.concatenateLineStrings(multiGeometry);

			assertThat(actual).extracting(LineString::getCoordinates).asInstanceOf(InstanceOfAssertFactories.LIST)
					.containsExactly(new Coordinate(1.1, 1.2), new Coordinate(2.1, 2.2));
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_LineString2() {
			logTestStart();

			LineString lineString1 = new LineString();
			lineString1.addToCoordinates(1.1, 1.2);
			lineString1.addToCoordinates(2.1, 2.2);

			LineString lineString2 = new LineString();
			lineString2.addToCoordinates(3.1, 3.2);
			lineString2.addToCoordinates(4.1, 4.2);

			MultiGeometry multiGeometry = new MultiGeometry();
			multiGeometry.addToGeometry(lineString1);
			multiGeometry.addToGeometry(lineString2);

			LineString actual = GeometryUtil.concatenateLineStrings(multiGeometry);

			assertThat(actual).extracting(LineString::getCoordinates).asInstanceOf(InstanceOfAssertFactories.LIST)
					.containsExactly(new Coordinate(1.1, 1.2), new Coordinate(2.1, 2.2), new Coordinate(3.1, 3.2),
							new Coordinate(4.1, 4.2));
		}
	}
}
