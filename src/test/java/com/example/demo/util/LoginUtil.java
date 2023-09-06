package com.example.demo.util;

import com.example.demo.config.GoPage;
import com.example.demo.config.Listener;
import com.example.demo.config.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginUtil extends PageObject {

    public static final boolean internal = true;
    public static final boolean external = false;
    public static final String admin = "robot.admin@automated.com";
    public static final String notAdmin = "robot.notadmin@automated.com";
    public static final String password = "P@ssw0rd";

    public LoginUtil() {
        super(null);
        browser.manage().window().maximize();
        browser.navigate().to("PageUrl.LOGIN_PAGE");
    }

    public GoPage initPage(String username, boolean isInternal) {
            return init(username, isInternal);
    }

    public GoPage init(String username, boolean isInternal) {
        try {
            fillUserLogin(username, password, browser);
            selectSiteAndLogin(isInternal, browser);

            new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlToBe("PageUrl.NAVIGATE"));

            return new GoPage(browser);
        } catch (Exception e) {
            Listener.logFail(browser, e,  "ERRO :: Não foi possivel efetuar o login");
            e.printStackTrace();
            throw new RuntimeException("ERRO :: Não foi possivel efetuar o login");
        }
    }

    public void fillUserLogin(String username, String password, WebDriver browser) {
        try {
            WebElement user = new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id("username")));
            user.click();
            user.sendKeys(username);

            WebElement pass = new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id("password")));
            pass.click();
            pass.sendKeys(password);

            new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id("login-button"))).click();
        } catch (Exception e) {
            Listener.logFail(browser, e,  "ERRO :: Não foi possivel efetuar o login.");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void selectSiteAndLogin(boolean isInternal, WebDriver browser) {
        try {
            new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id("site-list"))).click();

            Thread.sleep(100);
            if (isInternal)
                new Actions(browser).sendKeys(Keys.DOWN, Keys.RETURN).perform();
            else
                new Actions(browser).sendKeys(Keys.DOWN, Keys.DOWN, Keys.RETURN).perform();

            Thread.sleep(500);
            new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id("site-login-button"))).click();
        } catch (Exception e) {
            Listener.logFail(browser, e,  "ERRO :: Não foi possivel efetuar o login.");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}