import org.junit.Test;
import play.mvc.Content;

import java.math.BigDecimal;
import java.util.Currency;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;


public class ApplicationTest {
    @Test
    public void renderTemplateWithoutValues() {
        Content html = views.html.index.render(null, null, null, null);
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("iWant2Exchange");
        assertThat(contentAsString(html)).contains("Convert");
    }
    @Test
    public void renderTemplateWithValues() {
        Content html = views.html.index.render(Currency.getInstance("EUR"), new BigDecimal(100), Currency.getInstance("JPY"), new BigDecimal(200));
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("EUR");
        assertThat(contentAsString(html)).contains("100");
        assertThat(contentAsString(html)).contains("JPY");
        assertThat(contentAsString(html)).contains("200");
    }
}
