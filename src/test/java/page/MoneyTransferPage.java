package page;

import com.codeborne.selenide.*;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {

    private final SelenideElement amount = $("[data-test-id=amount] input");
    private final SelenideElement fromField = $("[data-test-id=from] input");
    private final SelenideElement transfer = $("[data-test-id=action-transfer]");
    private final SelenideElement error = $("[data-test-id=error-notification]");

    public void moneyTransfer(DataHelper.CardInfo from, String amountToTransfer) {
        amount.setValue(amountToTransfer);
        fromField.setValue(from.getNumber());
        transfer.click();
        new DashboardPage();
    }

    public void getError() {
        error.shouldBe(Condition.visible);
    }
}