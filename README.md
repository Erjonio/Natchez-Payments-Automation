# Natchez Payments Automation

This repository contains the automated End-to-End (E2E) regression suite for verifying user authentication, wishlist management, and secure payment gateway integrations (Curbstone Credit Card and Sezzle Payment) on the Natchez website.

## 🛠️ Tech Stack & Environment
* **Language:** Java 23 (OpenJDK)
* **Framework:** Playwright 1.49.0, JUnit 5
* **Target Environment:** Staging / Test Site (`https://f2yt3gogmjqkq-main-bvxea6i.us-2.tst.site`)

---

## 📋 Test Case Specification

### TC-NATCHEZ-CURBSTONE-01: E2E Wishlist to Curbstone Checkout Flow with Shipping Assurance
**Title:** Verify complete user journey from login, wishlist-to-cart migration, promo code usage, Shipping Assurance activation, and successful Curbstone payment clearance.

**Execution Steps:**
1. **Authentication:** Log in using valid user staging credentials.
2. **Product Navigation:** Browse and select the target catalog item (*"Complex-Clothing Sample item"*).
3. **Wishlist Management:** Add the item to a dynamically created, unique test wishlist repository to isolate the run data.
4. **Cart Migration:** Move the product from the custom wishlist directly into the active checkout cart.
5. **Discount Application:** Apply promo code `E2F1V6X3` and ensure cart totals recalculate.
6. **Order Configurations:** Activate the **Shipping Assurance toggle** on the checkout options layout.
7. **Secure Payment Processing:** Use Curbstone Credit Card as the payment method, populate the form with test credentials utilizing a randomized CVV token, and process authorization.
8. **Fulfillment Verification:** Confirm the application handles the return handshakes smoothly and lands on the final order success screen.

### TC-NATCHEZ-SEZZLE-02: E2E Shared Wishlist to Sezzle Checkout Flow
**Title:** Verify user journey from shared wishlist checkout initiation, promo code usage, Shipping Assurance activation, and signing up on the Sezzle Sandbox popup environment.

**Execution Steps:**
1. **Wishlist Access:** Navigate directly to the shared wishlist repository and add items to the cart.
2. **Authentication & Cart Update:** Adjust product quantity to 20 units and log in with staging user credentials via the checkout funnel.
3. **Discount Application:** Activate promo code `E2F1V6X3` on the checkout confirm view.
4. **Order Configurations:** Activate the **Shipping Assurance toggle** on the checkout options layout.
5. **Gateway Redirect:** Accept the terms and conditions, select **Sezzle Payment**, and trigger the secure checkout popup window.
6. **Sezzle Sandbox Authentication:** Complete user registration using a randomized dynamic phone number, unique dynamic email, and simulate the simulated OTP and PIN step verifications.
7. **Billing Details:** Complete the address selection step by entering test location credentials and selecting the matching address option.
8. **Credit Card Authorization:** Populate the secure payment layout with test credit card credentials and a randomized dynamic CVV token.
9. **Finalized Screening & SSN Validation:** Handle the dynamic transitional steps, process the secure SSN and Confirm SSN verification requirements, and finalize the payment workflow.
10. **Fulfillment Verification:** Successfully redirect back to the parent web application context and confirm landing on the final `/checkout/finish` view.

### TC-NATCHEZ-CURBSTONE-03: E2E New User Shared Wishlist to Curbstone Checkout Flow
**Title:** Verify user journey from shared wishlist checkout initiation, registration funnel, address placement selection, promo code validation, Shipping Assurance activation, and checkout clearance via the Curbstone payment iframe gateway.

**Execution Steps:**
1. **Wishlist Access:** Navigate directly to the shared wishlist repository and add the item to the cart.
2. **Account Creation:** Redirect to the checkout registration screen and complete the signup process using dynamically generated user data and credentials.
3. **Billing Address Selection:** Fill out the shipping form and dynamically select the validated matching location from the automated lookup selector.
4. **Discount Application:** Apply promo code `E2F1V6X3` and ensure cart totals recalculate.
5. **Order Configurations:** Activate the **Shipping Assurance toggle** to append order validation protection.
6. **Gateway Authorization:** Accept terms, select **Curbstone Credit Card**, and access the embedded secure iframe context.
7. **Secure Card Entry:** Populate the secure iframe input layouts with test credit cards and a randomized dynamic CVV token.
8. **Fulfillment Verification:** Process payment via the interactive "PAY" controller and verify the application successfully loads the final `/checkout/finish` success context.

### TC-NATCHEZ-CURBSTONE-04: Negative Validation - Invalid Card CVV Code Length
**Title:** Verify that entering an incomplete or invalid CVV token length within the Curbstone iframe triggers a validation block and prevents order submission.

**Execution Steps:**
1. **Direct Catalog Navigation:** Bypass the high-load homepage by navigating directly to the `/Shop-All/` product listings context.
2. **Cart Initiation:** Add a valid product item to the shopping cart and trigger the checkout registration funnel.
3. **New User Registration:** Complete the registration layout by generating a randomized dynamic email and populating uniform password configurations.
4. **Address Lookup Completion:** Populated the street address locator and execute a clean click over the Google Autocomplete matching drop-down option.
5. **Gateway Engagement:** Transition through the checkout steps, select **Curbstone Credit Card** via its targeted structural label, and implement pacing controls to guarantee iframe load stability.
6. **Defective Credential Input:** Inside the Curbstone input frame, enter valid test credit cards but supply an insufficient **2-digit CVV token (`43`)**.
7. **Validation Assertion:** Click the interactive "PAY" action controller and verify that the descriptive error state message *"Card CVV code is too short."* becomes explicitly visible inside the iframe container.
8. **Fulfillment Rejection:** Assert that the active page framework successfully rejects checkout transition and does **not** land on the final `/checkout/finish` view.

---

## 📊 Test Execution History & Logs

Below is the compilation of the automation execution history loops, containing tracking order entries generated across both active payment integration test suites:

| Order ID | Gateway / Suite | Execution Time | Payment Status / Outcome |
| :--- | :--- | :--- | :--- |
| **10497** | Curbstone (Existing User) | June 23, 2026 at 4:21 PM | `Authorized` |
| **10498** | Curbstone (Existing User) | June 23, 2026 at 4:27 PM | `Authorized` |
| **10499** | Curbstone (Existing User) | June 23, 2026 at 4:32 PM | `Authorized` |
| **10517** | Sezzle Sandbox | June 24, 2026 at 2:43 PM | `Authorized` |
| **10520** | Sezzle Sandbox | June 24, 2026 at 3:28 PM | `Authorized` |
| **10521** | Sezzle Sandbox | June 24, 2026 at 3:47 PM | `Authorized` |
| **10528** | Curbstone (New User) | June 26, 2026 at 1:59 PM | `Authorized` |
| **10530** | Curbstone (New User) | June 26, 2026 at 2:11 PM | `Authorized` |
| **10531** | Curbstone (New User) | June 26, 2026 at 2:12 PM | `Authorized` |
---
