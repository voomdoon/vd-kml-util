package de.voomdoon.util.kml.geometry;

import java.util.Iterator;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.LineString;

/**
 * Removes redundant coordinates.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class LineStringCleaner {

	/**
	 * DOCME add JavaDoc for method removeDuplicateCoorinates
	 * 
	 * @param lineString
	 *            {@link LineString}
	 * @since 0.1.0
	 */
	public void clean(LineString lineString) {
		Iterator<Coordinate> iter = lineString.getCoordinates().iterator();

		Coordinate last = null;

		while (iter.hasNext()) {
			Coordinate curr = iter.next();

			if (last != null && curr.equals(last)) {
				iter.remove();
			}

			last = curr;
		}
	}
}
