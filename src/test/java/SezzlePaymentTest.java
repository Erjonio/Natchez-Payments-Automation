import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Test;
import java.util.regex.Pattern;
import java.util.concurrent.ThreadLocalRandom;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SezzlePaymentTest {

    @Test
    void testSezzlePayment() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.setDefaultTimeout(60000);
            page.setDefaultNavigationTimeout(60000);

            // Gjenerimi i të dhënave Random
            int randomDigits = ThreadLocalRandom.current().nextInt(1000, 10000);
            int randomCvv = ThreadLocalRandom.current().nextInt(100, 1000);

            String dynamicEmail = "testtest" + randomDigits + "@gmail.com";
            String dynamicPhone = "(714) 567-" + randomDigits;
            String dynamicCvv = String.valueOf(randomCvv);

            // 1. Shkuarja te artikujt e ndarë në Wishlist
            page.navigate("https://f2yt3gogmjqkq-main-bvxea6i.us-2.tst.site/wishlist/shared/2a336deed57032975ea369f3d54a078a");
            page.waitForTimeout(3000);

            Locator addToCartBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).first();
            addToCartBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            addToCartBtn.click();
            page.waitForTimeout(3000);

            Locator quantitySelect = page.locator("select.product-detail-quantity-select, select[name='quantity']").first();
            if (quantitySelect.isVisible()) {
                quantitySelect.selectOption("20");
                page.waitForTimeout(2000);
            }

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Go to checkout")).click();
            page.waitForTimeout(3000);

            // 2. Procesi i Log-in në Shopware
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Login here")).click();
            page.waitForTimeout(2000);

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address").setExact(true)).fill("erjon.januzaj@solution25.com");
            page.waitForTimeout(1000);

            page.locator("#loginPassword").fill("E1234567890-=j");
            page.waitForTimeout(1500);

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log in")).click();
            page.waitForTimeout(4000);

            page.navigate("https://f2yt3gogmjqkq-main-bvxea6i.us-2.tst.site/checkout/confirm");
            page.waitForTimeout(3000);

            // 3. Aktivizimi i Promo Code
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Promo code")).fill("E2F1V6X3");
            page.waitForTimeout(1000);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("E2F1V6X3")).click();
            page.waitForTimeout(3000);

            // 4. Pranimi i Termave dhe përzgjedhja e Sezzle
            page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I have read and accepted the")).check();
            page.waitForTimeout(1000);
            page.getByText("Sezzle Payment").click();
            page.waitForTimeout(2500);

            // 5. Klikimi i Submit si dhe kapja e Popup-it me kontrollet e rregulluara
            Page page1 = null;
            try {
                page1 = context.waitForPage(new BrowserContext.WaitForPageOptions().setTimeout(45000), () -> {
                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit order")).click();
                });
            } catch (Exception e) {
                String pageContent = page.content();
                if (pageContent.contains("Symfony Exception") || pageContent.contains("SezzleApiException")) {
                    System.err.println("BUG I ZBULUAR: Backend crash me SezzleApiException (502 Bad Gateway)!");
                    assertThat(page.getByText("Please complete the Sezzle popup before placing the order")).isHidden();
                }
                throw e;
            }

            // Procesi në dritaren e Sezzle Sandbox
            page1.setDefaultTimeout(60000);
            page1.waitForTimeout(4000);

            // 6. Plotësimi i të dhënave në Sezzle Sandbox
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Mobile Phone")).fill(dynamicPhone);
            page1.waitForTimeout(1500);
            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue").setExact(true)).click();
            page1.waitForTimeout(3000);

            Locator otpBox = page1.getByTestId("otp-input-box");
            otpBox.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            otpBox.fill("123123");
            page1.waitForTimeout(3000);

            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Legal First Name")).fill("test");
            page1.waitForTimeout(1000);
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Legal Last Name")).fill("tester");
            page1.waitForTimeout(1000);

            page1.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Month")).click();
            page1.waitForTimeout(1000);
            page1.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("January")).click();
            page1.waitForTimeout(1000);
            page1.getByTestId("date-day-input").fill("10");
            page1.waitForTimeout(1000);
            page1.getByTestId("date-year-input").fill("2000");
            page1.waitForTimeout(1500);

            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email")).fill(dynamicEmail);
            page1.waitForTimeout(1000);
            page1.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I confirm that I am of legal")).check();
            page1.waitForTimeout(1500);
            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
            page1.waitForTimeout(4000);

            page1.getByTestId("otp-input-box").fill("123123");
            page1.waitForTimeout(2000);
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("4-digit PIN").setExact(true)).fill("1235");
            page1.waitForTimeout(1000);
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Confirm 4-digit PIN")).fill("1235");
            page1.waitForTimeout(1500);
            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
            page1.waitForTimeout(4000);

            page1.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Street")).fill("777 b");
            page1.waitForTimeout(2000);
            page1.getByText("Bannock Street, Denver, CO, USA").click();
            page1.waitForTimeout(1500);
            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
            page1.waitForTimeout(4000);

            // 7. Mbushja e Kartës së Kreditit
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name on Card")).fill("test tester");
            page1.waitForTimeout(1000);
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Card Number")).fill("4242 4242 4242 4242");
            page1.waitForTimeout(1000);
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Expiration Date")).fill("10/30");
            page1.waitForTimeout(1000);
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Security Code (CVV)")).fill(dynamicCvv);

            page1.waitForTimeout(3000);
            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Card")).click();

            // INTEGRIMI I CODEGEN: Presim kalimin e faqes dhe klikojmë submit-payment-option
            page1.waitForTimeout(5000);
            Locator submitPaymentBtn = page1.getByTestId("submit-payment-option");
            submitPaymentBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            submitPaymentBtn.click();

            page1.waitForTimeout(3000);

            // 8. Pjesa e SSN
            Locator ssnInput = page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("SSN").setExact(true));
            ssnInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)); // Sigurohemi që fusha është gati

            ssnInput.fill("678-54-6789");
            page1.waitForTimeout(1000);
            page1.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Confirm SSN")).fill("678-54-6789");
            page1.waitForTimeout(2000);
            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

            page1.waitForTimeout(5000);

            // 9. Përfundimi i Urdhërpagesës në Sezzle
            Locator doneBtn = page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Done"));
            doneBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            doneBtn.click();
            page1.waitForTimeout(3000);

            page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Complete Order")).click();

            // 10. Verifikimi i suksesit në Shopware
            page.waitForURL("**/checkout/finish**");
            assertThat(page).hasURL(Pattern.compile(".*/checkout/finish.*"));

            browser.close();
        }
    }
}
