package controllers;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.math.BigDecimal;
import java.util.Currency;

import static play.data.DynamicForm.form;

public class Application extends Controller {
    public static Result index() {
        DynamicForm requestData = form().bindFromRequest();
        BigDecimal amount = new BigDecimal(requestData.get("amount"));
        Currency fromCurrency = Currency.getInstance(requestData.get("from"));
        Currency toCurrency = Currency.getInstance(requestData.get("to"));
        BigDecimal result = amount;

        return ok(index.render(fromCurrency, amount, toCurrency, result));
    }
}
