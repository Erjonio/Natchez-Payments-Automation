import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Test;
import java.util.regex.Pattern;
import java.util.concurrent.ThreadLocalRandom;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CurbstonePaymentTest {

    @Test
    void test() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.setDefaultTimeout(60000);
            page.setDefaultNavigationTimeout(60000);

            // 1. Login Flow
            page.navigate("https://f2yt3gogmjqkq-main-bvxea6i.us-2.tst.site/account/login");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address")).fill("erjoni1619@gmail.com");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue with password")).click();

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill("E1234567890-=j");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log in")).click();

            // 2. Navigate to Item
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Shop All")).click();
            page.getByLabel("3 /").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Complex-Clothing Sample item")).click();

            // 3. Two-Step Wishlist Interaction Sequence
            Locator wishlistButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to wishlist"));
            wishlistButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            // First click to initialize the slow background process
            wishlistButton.click();
            page.waitForTimeout(3000);

            // Dismiss the initial loading state by clicking neutral space
            page.mouse().click(150, 150);
            page.waitForTimeout(2000);

            // Second click on the heart icon to open the configuration layout cleanly
            wishlistButton.click();
            page.waitForTimeout(2000);

            // Click "Create new list" button inside the module tray
            Locator createListBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create new list"));
            createListBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            createListBtn.click();
            page.waitForTimeout(1500);

            // Fetch the specific list name input box
            Locator wishlistInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("New list name"));
            wishlistInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            wishlistInput.click();

            // Generate unique list name for this run
            String uniqueWishlistName = "test" + System.currentTimeMillis();
            wishlistInput.fill(uniqueWishlistName);
            page.waitForTimeout(1000);

            // Submit list creation step
            createListBtn.click();
            page.waitForTimeout(2000);

            // Close out the module overlays cleanly
            page.locator("span").filter(new Locator.FilterOptions().setHasText("+")).nth(2).click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close")).click();

            // 4. Cart & Checkout Move
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Account")).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Wish list")).click();
            page.getByText(uniqueWishlistName).click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Go to checkout")).click();

            // 5. Apply Promo Code
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Promo code")).fill("E2F1V6X3");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("E2F1V6X3")).click();
            page.waitForTimeout(1500);

            // 6. Check Legal Boxes FIRST
            page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I have read and accepted the")).check();
            page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I want immediate access to")).check();
            page.waitForTimeout(1000);

            // 7. Secure IFrame Payment Execution
            page.getByText("Add a different card just for").click();
            page.waitForTimeout(1500);

            FrameLocator paymentIFrame = page.locator("iframe").contentFrame();

            paymentIFrame.getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Card Number")).fill("5454545454545454");
            paymentIFrame.getByLabel("card_exp_month").selectOption("10");
            paymentIFrame.getByLabel("card_exp_year").selectOption("2034");

            String randomCvv = String.valueOf(ThreadLocalRandom.current().nextInt(100, 1000));
            paymentIFrame.getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("CVV")).fill(randomCvv);

            // Authorize the card inside the IFrame
            paymentIFrame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("PAY")).click();
            page.waitForTimeout(3000);

            // ONLY click submit order if the gateway hasn't already auto-forwarded us to the success page
            if (!page.url().contains("/checkout/finish")) {
                Locator submitOrderBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit order"));
                if (submitOrderBtn.isVisible()) {
                    submitOrderBtn.click();
                }
            }

            // Fallback: Handle the dynamic order/edit gateway loop page context if triggered
            if (page.url().contains("/account/order/edit/")) {
                if (page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I have read and accepted the")).isVisible()) {
                    page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I have read and accepted the")).check();
                    page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I want immediate access to")).check();
                }

                page.getByText("Add a different card just for").click();
                page.waitForTimeout(1500);

                FrameLocator fallbackIFrame = page.locator("iframe").contentFrame();
                fallbackIFrame.getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Card Number")).fill("5454545454545454");
                fallbackIFrame.getByLabel("card_exp_month").selectOption("8");
                fallbackIFrame.getByLabel("card_exp_year").selectOption("2034");

                String dynamicFallbackCvv = String.valueOf(ThreadLocalRandom.current().nextInt(100, 1000));
                fallbackIFrame.getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("CVV")).fill(dynamicFallbackCvv);

                fallbackIFrame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("PAY")).click();
                page.waitForTimeout(3000);
            }

            // 8. Optimized Content-Based Assertion
            // Wait for the final layout text confirmation or the finish landing keyword to appear
            Locator thankYouText = page.getByText("Thank you", new Page.GetByTextOptions().setExact(false));
            try {
                thankYouText.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
                assertThat(thankYouText).isVisible();
            } catch (Exception e) {
                // Alternative check: URL confirmation
                assertThat(page).hasURL(Pattern.compile(".*/checkout/finish.*"));
            }

            browser.close();
        }
    }
}
