package com.example.demo.service;

import com.example.demo.config.Listener;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService {

    public void fillField(WebDriver browser, String id, String text) {
        try {
            WebElement field = new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
            field.clear();
            field.sendKeys(text);
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel preencher o campo: " + id);
            e.printStackTrace();
            throw new RuntimeException("ERRO :: Não foi possivel preencher o campo: " + id);
        }
    }

    public void findButton(WebDriver browser, String id) {
        try {
            new WebDriverWait(browser, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
            new WebDriverWait(browser, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.id(id))).click();
        } catch (StaleElementReferenceException e) {
            new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//*[@id='%s']", id)))).click();
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel encontrar o botão: " + id);
            e.printStackTrace();
            throw new RuntimeException("ERRO :: Não foi possivel encontrar o botão: " + id);
        }
    }

    public List<String> isListNamesInOrder(WebDriver browser, boolean inOrder) {
        try {
            List<WebElement> userList = new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("column-user")));
            List<String> users = new ArrayList<>();

            if (!inOrder) {
                for (WebElement user : userList) {
                    users.add(user.getText());
                }
                users.sort(String.CASE_INSENSITIVE_ORDER);
                return users;

            } else
                for (WebElement u : userList) {
                    users.add(u.getText());
                }

            return users;
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar ordenação.");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean forceErrorField(WebDriver browser, String idField, String idErro, String text) {
        try {
            WebElement field = new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id(idField)));
            field.click();
            field.clear();
            field.sendKeys("e");
            field.sendKeys(Keys.BACK_SPACE);

            return checkTextReturn(browser, idErro, text);
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar messagem de erro do campo: " + idField);
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkTextReturn(WebDriver browser, String id, String text) {
        try {
            return new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions
                            .visibilityOfElementLocated(By.xpath(String.format("//*[@id='%s']//*[.='%s']", id, text))))
                    .getText().equals(text);
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Mensagem de retorno diferente do esperado: " + text);
            return false;
        }
    }

    public boolean checkModal(WebDriver browser, String title) {
        try {
            WebElement e = new WebDriverWait(browser, Duration.ofSeconds(5)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//*[.='%s']", title))));
            return e.getText().contains(title);
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Formulário diferente do esperado: " + title);
            e.printStackTrace();
            throw new RuntimeException("ERRO :: Formulário diferente do esperado: " + title);
        }
    }

    public void paste(WebDriver browser, String id) {
        try {
            WebElement field = browser.findElement(By.id(id));
            field.clear();
            field.click();
            new Actions(browser).keyDown(Keys.CONTROL).sendKeys("V").keyUp(Keys.CONTROL).build().perform();
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel colar o token");
            e.printStackTrace();
            throw new RuntimeException("ERRO :: Não foi possivel colar o token");
        }
    }

    public String getValueField(WebDriver browser, String id) {
        try {
            WebElement value = new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
            return value.getAttribute("value");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel capturar o conteúdo do campo " + id);
            e.printStackTrace();
            throw new RuntimeException("ERRO :: Não foi possivel capturar o conteúdo do campo " + id);
        }
    }

    public void selectType(WebDriver browser, String type) {
        try {
            if (Objects.equals(type, "MACHINE"))
                new Actions(browser).sendKeys(Keys.DOWN, Keys.RETURN).perform();
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel selecionar o tipo de usuário.");
            e.printStackTrace();
            throw new RuntimeException("ERRO :: Não foi possivel selecionar o tipo de usuário.");
        }
    }

    public String checkStringValue(WebDriver browser, String id) {
        try {
            String user = browser.findElement(By.id(id)).getText();

            if (user.contains("Usuário:"))
                return formatterNameUser(user);
            else
                return browser.findElement(By.id(id)).getText();

        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel capturar o texto do id: " + id);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean isDeleted(WebDriver browser, String id, String name) {
        try {
            browser.findElement(By.id("button-refresh")).click();
            return new WebDriverWait(browser, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id(id))).getText().equals(name);
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar se produto foi deletado.");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private String formatterNameUser(String nameUser) {
        return nameUser.substring(9).trim();
    }

    public boolean isCurrentPage(WebDriver browser) {
        try {
            return browser.getCurrentUrl().equals("www.currentpage.com.br");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Página corrente inválida.");
            e.printStackTrace();
            return false;
        }
    }
}
