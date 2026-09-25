package com.ecommerce.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object Model (POM) for Customer Authentication (Epic ECOM-EP-01).
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-btn");
    private final By loginErrorBanner = By.id("login-error");
    private final By userGreeting = By.id("user-greeting");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
    }

    public String getErrorMessage() {
        return getText(loginErrorBanner);
    }

    public String getGreetingText() {
        return getText(userGreeting);
    }
}
