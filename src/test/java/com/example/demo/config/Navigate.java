package com.example.demo.config;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Navigate {

    public void navigatingThroughMenu(WebDriver browser, String idMenu) {
        try {
            new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id(idMenu))).click();
        } catch (Exception e) {
            Listener.logFail(browser, e,  "Não foi possível acessar o menu: " + idMenu);
            e.printStackTrace();
            throw new RuntimeException("Não foi possível acessar o menu: " + idMenu);
        }
    }
}
