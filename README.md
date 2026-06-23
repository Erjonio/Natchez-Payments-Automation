# Shopware 6 Curbstone Payment Automation

This repository contains the automated End-to-End (E2E) regression suite for verifying user authentication, wishlist management, and the secure Curbstone Credit Card payment gateway integration on the Shopware 6 platform.

## 🛠️ Tech Stack & Environment
* **Language:** Java 23 (OpenJDK)
* **Framework:** Playwright 1.49.0, JUnit 5
* **Target Environment:** Staging / Test Site (`https://f2yt3gogmjqkq-main-bvxea6i.us-2.tst.site`)

---

## 📋 Test Case Specification

### TC-SHOPWARE-CURBSTONE-01: E2E Wishlist to Curbstone Checkout Flow with Shipping Assurance
**Title:** Verify complete user journey from login, wishlist-to-cart migration, promo code usage, Shipping Assurance activation, and successful Curbstone payment clearance.

### Execution Steps:
1. **Authentication:** Log in using valid user staging credentials.
2. **Product Navigation:** Browse and select the target catalog item (*"Complex-Clothing Sample item"*).
3. **Wishlist Management:** Add the item to a dynamically created, unique test wishlist repository to isolate the run data.
4. **Cart Migration:** Move the product from the custom wishlist directly into the active checkout cart.
5. **Discount Application:** Apply promo code `E2F1V6X3` and ensure cart totals recalculate.
6. **Order Configurations:** Activate the **Shipping Assurance toggle** on the checkout options layout.
7. **Secure Payment Processing:** Use Curbstone Credit Card as the payment method, populate the form with test credentials utilizing a randomized CVV token, and process authorization.
8. **Fulfillment Verification:** Confirm the application handles the return handshakes smoothly and lands on the final order success screen.

---

## 📊 Test Execution History & Logs

The automation script ran successfully on **June 23, 2026**. Below are the tracking numbers generated during the verification loops:

| Order ID | Execution Time | Payment Status |
| :--- | :--- | :--- |
| **10497** | 4:21 PM | `Authorized` |
| **10498** | 4:27 PM | `Authorized` |
| **10499** | 4:32 PM | `Authorized` |
