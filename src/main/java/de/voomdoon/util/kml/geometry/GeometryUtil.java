package de.voomdoon.util.kml.geometry;

import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import lombok.experimental.UtilityClass;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
@UtilityClass
public class GeometryUtil {

	/**
	 * DOCME add JavaDoc for method createLineString
	 * 
	 * @param multiGeometry
	 * @return
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
