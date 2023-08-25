package com.example.demo.util;

import com.example.demo.config.Listener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CommonUtil {

    public void filterByThisString(WebDriver browser, String filteredText) {
        browser.findElement(By.id("filter-txt")).clear();
        browser.findElement(By.id("filter-txt")).sendKeys(filteredText);
        clickButtonById(browser, "button-refresh");
    }

    public void clickButtonById(WebDriver browser, String buttonId) {
        try {
            new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id(buttonId)));
            browser.findElement(By.id(buttonId)).click();
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel encontrar o botão: " + buttonId);
            e.printStackTrace();
            throw new RuntimeException("ERRO :: Não foi possivel encontrar o botão: " + buttonId);
        }
    }

    public void clickButtonByCssSelector(WebDriver browser, String css) {
        try {
            new WebDriverWait(browser, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.cssSelector(css))).click();
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel encontrar o botão: " + css);
        }
    }

}
