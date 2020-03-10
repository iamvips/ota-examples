import dataentities.*;
import pages.*;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestLoanTest {

    private WebDriver driver;

    @Before
    public void createBrowser() {

        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        User john = User.builder().username("john").password("demo").build();

        new LoginPage(driver).
            load().
            loginAs(john);
    }

    @Test
    public void requestLoan_withInsufficientFunds_shouldBeDenied() {

        LoanRequest aLoanRequestWithInsufficientFunds =
            LoanRequest.builder().loanAmount("10000").downPayment("100").fromAccountId("54321").build();

        new RequestLoanPage(driver).
            load().
            submit(aLoanRequestWithInsufficientFunds);

        String theDisplayedApplicationResult = new RequestLoanResultPage(driver).getLoanApplicationResult();

        assertThat(theDisplayedApplicationResult).isEqualTo("Denied");
    }

    @Test
    public void requestLoan_withSufficientFunds_shouldBeApproved() {

        LoanRequest aLoanRequestWithSufficientFunds =
            LoanRequest.createALoanRequestWithSufficientFunds();

        new RequestLoanPage(driver).
            load().
            submit(aLoanRequestWithSufficientFunds);

        String theDisplayedApplicationResult = new RequestLoanResultPage(driver).getLoanApplicationResult();

        assertThat(theDisplayedApplicationResult).isEqualTo("Approved");
    }

    @After
    public void closeBrowser() {

        driver.quit();
    }
}