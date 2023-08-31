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
	 * @since 0.1.0
	 */
	private double altitudeThreshold;

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

			if (last != null && isEqual(last, curr)) {
				iter.remove();
			}

			last = curr;
		}
	}

	/**
	 * @return altitudeThreshold
	 * @since 0.1.0
	 */
	public double getAltitudeThreshold() {
		return altitudeThreshold;
	}

	/**
	 * @param altitudeThreshold
	 *            altitudeThreshold
	 * @since 0.1.0
	 */
	public void setAltitudeThreshold(double altitudeThreshold) {
		this.altitudeThreshold = altitudeThreshold;
	}

	/**
	 * DOCME add JavaDoc for method isEqual
	 * 
	 * @param last
	 * @param curr
	 * @return
	 * @since 0.1.0
	 */
	private boolean isEqual(Coordinate last, Coordinate curr) {
		if (altitudeThreshold > 0 && curr.getLongitude() == last.getLongitude()
				&& curr.getLatitude() == last.getLatitude()) {
			return Math.abs(curr.getAltitude() - last.getAltitude()) <= altitudeThreshold;
		}

		return curr.equals(last);
	}
}
