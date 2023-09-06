package com.example.demo.config;

import com.example.demo.page.UserPage;
import org.openqa.selenium.WebDriver;

public class GoPage extends PageObject implements GoTo {

    public GoPage(WebDriver browser) {
        super(browser);
    }

    private Navigate navigate = new Navigate();

    @Override
    public UserPage goToUserPage() {
        navigate.navigatingThroughMenu(browser, "MENU.REGISTER");
        return new UserPage(browser);
    }
}
