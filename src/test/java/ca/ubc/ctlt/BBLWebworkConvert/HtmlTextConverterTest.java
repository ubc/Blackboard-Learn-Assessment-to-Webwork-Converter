package ca.ubc.ctlt.BBLWebworkConvert;

import ca.ubc.ctlt.BBLWebworkConverter.HtmlTexConverter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by compass on 13-10-10.
 */
public class HtmlTextConverterTest {
    @BeforeClass
    public void setUp() {
        // code that will be invoked when this test is instantiated
    }

    @Test
    public void nornmalConvertTest() {
        String expected = "[n] moles of an ideal gas at [T] [`^{o}`]C expands *isothermally* and *reversibly* from an initial volume of [I] L to a final volume of [F] L.  Calculate _q_ for this expansion in J.";

        assertEquals(HtmlTexConverter.convert("<p>[n] moles of an ideal gas at [T] <sup>o</sup>C expands <strong>isothermally</strong> and <strong>reversibly</strong> from an initial volume of [I] L to a final volume of [F] L.  Calculate <em>q</em> for this expansion in J.</p>"),
                expected);
    }
}
