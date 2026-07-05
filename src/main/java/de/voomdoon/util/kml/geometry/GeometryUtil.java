package de.voomdoon.util.kml.geometry;

import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;

/**
 * Utility for {@link Geometry}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public final class GeometryUtil {

	/**
	 * Prevents instantiation.
	 */
	private GeometryUtil() {
	}

	/**
	 * Concatenates all {@link LineString} of a {@link MultiGeometry} into a single {@link LineString}.
	 * 
	 * @param multiGeometry
	 *            {@link MultiGeometry} with {@link LineString}
	 * @return {@link LineString}
	 * @since 0.1.0
	 */
	public static LineString concatenateLineStrings(MultiGeometry multiGeometry) {
		LineString result = new LineString();

		for (Geometry geometry : multiGeometry.getGeometry()) {
			if (geometry instanceof LineString ls) {
				result.getCoordinates().addAll(ls.getCoordinates());
			} else {
				throw new UnsupportedOperationException(
						"Method 'concatenateLineStrings' not implemented for " + geometry + "!");
			}
		}

		return result;
	}
}
