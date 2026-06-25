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

---

## 📊 Test Execution History & Logs

Below is the compilation of the automation execution history loops, containing tracking order entries generated across both active payment integration test suites:

| Order ID | Gateway / Suite | Execution Time | Payment Status |
| :--- | :--- | :--- | :--- |
| **10497** | Curbstone | June 23, 2026 at 4:21 PM | `Authorized` |
| **10498** | Curbstone | June 23, 2026 at 4:27 PM | `Authorized` |
| **10499** | Curbstone | June 23, 2026 at 4:32 PM | `Authorized` |
| **10517** | Sezzle Sandbox | June 24, 2026 at 2:43 PM | `Authorized` |
| **10520** | Sezzle Sandbox | June 24, 2026 at 3:28 PM | `Authorized` |
| **10521** | Sezzle Sandbox | June 24, 2026 at 3:47 PM | `Authorized` |

---
