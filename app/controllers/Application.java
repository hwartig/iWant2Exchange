package controllers;

import org.codehaus.jackson.JsonNode;
import play.Play;
import play.data.DynamicForm;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static play.data.DynamicForm.form;

public class Application extends Controller {
    public static Result index() {
        DynamicForm requestData = form().bindFromRequest();
        String stringAmount = requestData.get("amount");
        if(stringAmount == null) {
            return ok(index.render(null, null, null, null));
        }

        final BigDecimal amount = new BigDecimal(stringAmount);
        final Currency fromCurrency = Currency.getInstance(requestData.get("from"));
        final Currency toCurrency = Currency.getInstance(requestData.get("to"));
        final String api_key = Play.application().configuration().getString("openexchangerates.api_key");

        return async(
                WS.url(Play.application().configuration().getString("openexchangerates.url")).setQueryParameter("app_id", api_key).get().map(
                        new F.Function<WS.Response, Result>() {
                            public Result apply(WS.Response response) {
                                JsonNode rates = response.asJson().findPath("rates");

                                //TODO: Move conversion to Service or Helper
                                BigDecimal rateToUSD = BigDecimal.ONE.divide(new BigDecimal(String.valueOf(rates.findPath(fromCurrency.getCurrencyCode()))), 10, RoundingMode.HALF_UP);
                                BigDecimal rateFromUSD = new BigDecimal(String.valueOf(rates.findPath(toCurrency.getCurrencyCode())));
                                BigDecimal result = amount.multiply(rateToUSD).multiply(rateFromUSD).setScale(2, RoundingMode.UP);

                                return ok(index.render(fromCurrency, amount, toCurrency, result));
                            }
                        }
                )
        );
    }
}
