package de.voomdoon.util.kml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.micromata.opengis.kml.v_2_2_0.Document;
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
		 * DOCME add JavaDoc for method test
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test_Placemark1_Document_styleSelector() throws Exception {
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
		void test_Placemark1_Placemark_styleUrl() throws Exception {
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
		void test_Placemark2_Document_styleSelector() throws Exception {
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
	}
}
