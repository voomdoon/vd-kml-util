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
	 * @since 0.1.0
	 */
	private double epsilon = 1E-9;

	/**
	 * Removes redundant coordinates from {@link LineString}.
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
	 * @param altitudeThreshold
	 *            altitudeThreshold
	 * @since 0.1.0
	 */
	public void setAltitudeThreshold(double altitudeThreshold) {
		this.altitudeThreshold = altitudeThreshold;
	}

	/**
	 * @param last
	 *            {@link Coordinate}
	 * @param curr
	 *            {@link Coordinate}
	 * @return {@code trus} if equal, {@code false} otherwise
	 * @since 0.1.0
	 */
	private boolean isEqual(Coordinate last, Coordinate curr) {
		// mutation with changed conditional boundary no covered (will be handled by calculation)
		if (altitudeThreshold > 0 //
				&& curr.getLongitude() == last.getLongitude()//
				&& curr.getLatitude() == last.getLatitude()) {
			// mutation with changed conditional boundary no covered, due to floating point precision
			// using BigDecimal would have huge performance impact
			return Math.abs(curr.getAltitude() - last.getAltitude()) <= altitudeThreshold + epsilon;
		}

		return curr.equals(last);
	}
}
