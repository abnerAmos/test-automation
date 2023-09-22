package com.example.demo.config;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.awt.*;
import java.io.File;
import java.io.IOException;

// Ouvinte que captura todos os tipos de status dos testes e adiciona um log a esses status.
public class Listener implements ITestListener {

    public static ExtentReports extentReports;
    public static ExtentTest test;

    // Separa cada report em uma thread juntamente com os testes em thread.
    private static final ThreadLocal<ExtentTest> thread = new ThreadLocal<>();

    private static Exception ex;
    private static Media media;

    private Exception getException() {
        return ex;
    }

    private Media getMedia() {
        return media;
    }

    // Método cria Status de LOG com descrição dos testes
    public static void testStart(String nameTest, String description, String author, String category) {
        test = extentReports.createTest(nameTest, description)
                .assignAuthor(formatterNameUser(author))
                .assignCategory(category);
        thread.set(test);
        thread.get().info(MarkupHelper.createLabel("EXECUÇÃO DO MÉTODO INICIADO.", ExtentColor.BLUE));
    }

    // Método é executado uma vez antes dos testes, ao qual cria o arquivo HTML.
    @Override
    public void onStart(ITestContext context) {
        extentReports = new ExtentReports();
        extentReports = ReportConfig.createInstance("report.html");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        thread.get().skip(MarkupHelper.createLabel("Teste desabilitado.", ExtentColor.ORANGE));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        thread.get().pass(MarkupHelper.createLabel("Teste concluído com sucesso.", ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.fail(getException(), getMedia());
    }

    // Método é executado após a finalização de todos os testes de todas as classes
    // Fecha o arquivo HTML e abre automaticamente após a finalização
    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
        try {
            Desktop.getDesktop().browse(new File("reports\\report.html").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método estático que captura os logs dos testes
    // PS. Só é executado caso um teste ja tenha sido iniciado, caso contrário retorna null
    public static void logPass(String log) {
        thread.get().pass(log);
    }

    public static void logFail(WebDriver browser, Exception e, String log) {
        ex = e;
        media = MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(browser)).build();
        thread.get().fail(log);
    }

    public static void logInfo(String log) {
        thread.get().info(MarkupHelper.createLabel(log, ExtentColor.BLUE));
    }

    public static String takeScreenshot(WebDriver browser) {
        return ((TakesScreenshot)browser).getScreenshotAs(OutputType.BASE64);
    }

    private static String formatterNameUser(String nameUser) {
        String newName = nameUser.substring(9);
        return newName;
    }
}
