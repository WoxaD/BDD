package test;

import data.DataHelper;
import org.junit.jupiter.api.*;
import page.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldBeTransferredFromTheFirstCardToTheSecondCard() {
        var dashboardPage = new DashboardPage();

        int expected2 = dashboardPage.getCardBalance("2") + 5000;
        int expected1 = dashboardPage.getCardBalance("1") - 5000;

        dashboardPage.getMoneyTransferFromFirstToSecond();
        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("1"), "5000");

        int actual1 = dashboardPage.getCardBalance("1");
        int actual2 = dashboardPage.getCardBalance("2");

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldBeTransferredFromTheSecondCardToTheFirstCard() {
        var dashboardPage = new DashboardPage();

        int expected1 = dashboardPage.getCardBalance("1") + 1000;
        int expected2 = dashboardPage.getCardBalance("2") - 1000;

        dashboardPage.getMoneyTransferFromSecondToFirst();
        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("2"), "1000");

        int actual1 = dashboardPage.getCardBalance("1");
        int actual2 = dashboardPage.getCardBalance("2");

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldNotBePassedINoAccountIsSpecified() {
        var dashboardPage = new DashboardPage();

        dashboardPage.getMoneyTransferFromSecondToFirst();
        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo(""), "1000");

        moneyTransferPage.getError();
    }

    @Test
    void shouldNotTransferMoreThanTheBalance() {
        var dashboardPage = new DashboardPage();
        String balance = String.valueOf(dashboardPage.getCardBalance("1") + 100);

        int expected1 = dashboardPage.getCardBalance("1");
        int expected2 = dashboardPage.getCardBalance("2");

        dashboardPage.getMoneyTransferFromFirstToSecond();
        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("1"), balance);

        int actual1 = dashboardPage.getCardBalance("1");
        int actual2 = dashboardPage.getCardBalance("2");

        assertNotEquals(expected1, actual1);
        assertNotEquals(expected2, actual2);
    }
}

