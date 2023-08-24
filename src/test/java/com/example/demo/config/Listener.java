package com.example.demo.config;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
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

    // Método cria Status de LOG com descrição dos testes
    public static void testStart(String nameTest, String description, String author, String category) {
        test = extentReports.createTest(nameTest, description)
                .assignAuthor(formatterNameUser(author))
                .assignCategory(category);
        test.info(MarkupHelper.createLabel("EXECUÇÃO DO MÉTODO INICIADO.", ExtentColor.BLUE));
    }

    // Método é executado uma vez antes dos testes, ao qual cria o arquivo HTML.
    @Override
    public void onStart(ITestContext context) {
        extentReports = new ExtentReports();
        extentReports = ReportConfig.createInstance("report.html");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.skip(MarkupHelper.createLabel("Teste desabilitado.", ExtentColor.ORANGE));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass(MarkupHelper.createLabel("Teste concluído com sucesso.", ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.fail(MarkupHelper.createLabel("Teste falhou.", ExtentColor.RED));
        test.fail(result.getThrowable());
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
    public static void logTest(String status, String log) {
        if (status.equals("pass"))
            test.pass(log);
        if (status.equals("fail"))
            test.fail(log);
        if (status.equals("info"))
            test.info(MarkupHelper.createLabel(log, ExtentColor.BLUE));
    }

    private static String formatterNameUser(String nameUser) {
        String newName = nameUser.substring(9);
        return newName;
    }
}
