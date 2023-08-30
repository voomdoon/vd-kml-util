package de.voomdoon.util.kml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.voomdoon.testing.logging.tests.LoggingCheckingTestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class KmlStyleUtilTest {

	/**
	 * DOCME add JavaDoc for KmlStyleUtilTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class SetStyleUrlTest extends LoggingCheckingTestBase {

		/**
		 * DOCME add JavaDoc for method test_error_equalIdButDifferent
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_error_equalIdButDifferent_lineStyle_with() throws Exception {
			logTestStart();

			Document document = new Document();

			Style style1 = new Style();
			style1.setLineStyle(new LineStyle().withWidth(1));
			style1.setId("test");

			Style style2 = new Style();
			style2.setLineStyle(new LineStyle().withWidth(2));
			style2.setId("test");

			KmlStyleUtil.setStyleUrl(new Placemark(), style1, document);

			Placemark placemark = new Placemark();

			assertThrows(IllegalArgumentException.class, () -> KmlStyleUtil.setStyleUrl(placemark, style2, document));
		}

		/**
		 * DOCME add JavaDoc for method test
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_Placemark1_DocumentStyleSelectorSet() throws Exception {
			logTestStart();

			Document document = new Document();
			Placemark placemark = new Placemark();
			Style style = new Style();
			style.setId("test");

			KmlStyleUtil.setStyleUrl(placemark, style, document);

			assertThat(document.getStyleSelector()).hasSize(1);
		}

		/**
		 * DOCME add JavaDoc for method test
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_Placemark1_PlacemarkStyleUrlSet() throws Exception {
			logTestStart();

			Document document = new Document();
			Placemark placemark = new Placemark();
			Style style = new Style();
			style.setId("test");

			KmlStyleUtil.setStyleUrl(placemark, style, document);

			assertThat(placemark.getStyleUrl()).isEqualTo("#test");
		}

		/**
		 * DOCME add JavaDoc for method test
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_Placemark2_Style1_DocumentStyleSelector1set() throws Exception {
			logTestStart();

			Document document = new Document();
			Placemark placemark1 = new Placemark();
			Placemark placemark2 = new Placemark();
			Style style = new Style();
			style.setId("test");

			KmlStyleUtil.setStyleUrl(placemark1, style, document);
			KmlStyleUtil.setStyleUrl(placemark2, style, document);

			assertThat(document.getStyleSelector()).hasSize(1);
		}

		/**
		 * DOCME add JavaDoc for method test
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_Placemark2_Style2_DocumentStyleSelector2set() throws Exception {
			logTestStart();

			Document document = new Document();

			Placemark placemark1 = new Placemark();
			Style style1 = new Style();
			style1.setId("test1");

			KmlStyleUtil.setStyleUrl(placemark1, style1, document);

			Placemark placemark2 = new Placemark();
			Style style2 = new Style();
			style2.setId("test2");

			KmlStyleUtil.setStyleUrl(placemark2, style2, document);

			assertThat(document.getStyleSelector()).hasSize(2);
		}
	}
}
