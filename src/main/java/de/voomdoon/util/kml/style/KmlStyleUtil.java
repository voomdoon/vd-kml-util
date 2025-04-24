package de.voomdoon.util.kml.style;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import lombok.experimental.UtilityClass;

/**
 * Utility for {@link Style}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
@UtilityClass
public class KmlStyleUtil {

	/**
	 * Sets {@link Placemark#setStyleUrl(String)} and ensures the {@link Style} is added to the {@link Document}.
	 * 
	 * @param placemark
	 *            {@link Placemark}
	 * @param style
	 *            {@link Style}
	 * @param document
	 *            {@link Document}
	 * @since 0.1.0
	 */
	public static void setStyleUrl(Placemark placemark, Style style, Document document) {
		add(document, style);
		placemark.setStyleUrl(getUrl(style));
	}

	/**
	 * @param document
	 *            {@link Document}
	 * @param style
	 *            {@link Style}
	 * @since 0.2.0
	 */
	private static void add(Document document, Style style) {
		boolean found = false;

		for (StyleSelector ss : document.getStyleSelector()) {
			if (ss.getId().equals(style.getId())) {
				if (!ss.equals(style)) {
					throw new IllegalArgumentException("Inconsistent Styles with id " + style.getId() + "!");
				}

				found = true;
			}
		}

		if (!found) {
			document.getStyleSelector().add(style);
		}
	}

	/**
	 * @param style
	 *            {@link Style}
	 * @return URL {@link String}
	 * @since 0.2.0
	 */
	private static String getUrl(Style style) {
		return "#" + style.getId();
	}
}
