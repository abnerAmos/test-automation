package com.example.demo.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class PageObject {

    protected WebDriver browser;

    public PageObject(WebDriver browser) {

        // ChromeOptions, serve para adicionar argumentos ao abrir o browser, inclusive o modo Headless

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--proxy-server='direct://'");
        options.addArguments("--proxy-bypass-list=*");
        options.addArguments("--start-maximized");
        options.addArguments("--headless=new");

        // Driver para conexão com o navegador
        WebDriverManager.chromedriver().setup();

        // Instancia do Driver do Chrome
        if (browser != null)
            this.browser = browser;
        else
            this.browser = new ChromeDriver(options);
    }

    public void closeBrowser() {
        this.browser.quit();
    }
}
