# E-Commerce Application QA & Selenium Test Automation Suite

**Tech Stack:** `Selenium WebDriver 4` | `Java` | `TestNG` | `Page Object Model (POM)` | `JUnit 5` | `Maven` | `Jira & Agile`

## 1. Project Overview
This repository implements an end-to-end **QA & Selenium Test Automation Regression Suite** for the **TechMart E-Commerce Portal**, engineered using the **Page Object Model (POM)** design pattern. It validates critical customer workflows across 3 Agile Epics:
- **`ECOM-EP-01` (Authentication & Access Control)**: Valid login, locked-out user enforcement, and invalid credential handling.
- **`ECOM-EP-02` (Shopping Cart & Quantity Boundary Rules)**: Adding items, dynamic subtotal updates, and strict **Boundary Value Analysis (BVA)** (`Min=1`, `Max=10`, invalid boundaries `0` and `11`).
- **`ECOM-EP-03` (Checkout, Promo Code & Order Confirmation)**: Shipping address validation, **Postal Code length BVA** (`5 to 6 numeric digits`, invalid boundaries `4` and `7 digits`), `SAVE10` promo discount math, and order confirmation (`ORD-2026-8492`).

---

## 2. Project Folder Structure (Page Object Model)
```text
ecommerce-qa-suite/
├── pom.xml                                        # Maven dependencies (Selenium 4, TestNG, JUnit 5)
├── testng.xml                                     # TestNG XML regression suite runner
├── src/
│   ├── main/java/com/ecommerce/qa/
│   │   ├── pages/                                 # Page Object Model (POM) Layer
│   │   │   ├── BasePage.java                      # Explicit waits (WebDriverWait) & reusable actions
│   │   │   ├── LoginPage.java                     # Login screen locators & actions
│   │   │   ├── CartPage.java                      # Catalog & Cart quantity boundary actions
│   │   │   └── CheckoutPage.java                  # Shipping, Promo Code & Order Confirmation actions
│   │   └── utils/
│   │       └── CheckoutBoundaryValidator.java     # Core pricing & boundary validation rules
│   └── test/
│       ├── java/com/ecommerce/qa/tests/
│       │   ├── BaseTest.java                      # WebDriver setup (@BeforeMethod) & teardown (@AfterMethod)
│       │   ├── CheckoutWorkflowTest.java          # 14 TestNG UI Regression tests (Functional, Negative, BVA)
│       │   └── CheckoutBoundaryValidatorJUnitTest.java # 3 JUnit 5 fast unit tests
│       └── resources/webapp/
│           └── index.html                         # Self-contained TechMart E-Commerce storefront
```

---

## 3. How to Run the Tests

### Run Headless (Fast CI / Terminal Mode)
```bash
mvn clean test
```

### Run with Visible Chrome Browser Window (Demo / Interview Mode)
```bash
mvn test -Dheadless=false
```

### Run in IntelliJ IDEA
1. Open **IntelliJ IDEA** -> **File -> Open** -> Select `c:\Users\yasha\Documents\ISTQB\ecommerce-qa-suite\pom.xml`.
2. Right-click `testng.xml` -> **Run '.../testng.xml'** to execute the TestNG Selenium POM suite.
3. Right-click `CheckoutBoundaryValidatorJUnitTest.java` -> **Run** to execute the JUnit 5 unit tests.
